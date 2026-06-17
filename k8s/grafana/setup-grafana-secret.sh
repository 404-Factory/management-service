#!/usr/bin/env bash
#
# Grafana 자체 배포(Helm/PVC/ingress/admin secret)는 Terraform이 담당한다.
# 이 스크립트는 배포 "후" 세 가지를 수행한다:
#   1) datasource 프로비저닝 — provisioning yml을 Secret으로 만들고 grafana sidecar가 로드
#      (creds가 k8s Secret에만 존재 → tfstate/로그에 노출 없음.
#       Terraform Helm 값에 sidecar.datasources.enabled=true 필요)
#   2) management-snapshot ServiceAccount 토큰 발급 → grafana-secret
#      (management 서비스가 스냅샷 생성 시 사용하는 GRAFANA_TOKEN)
#   3) management-service rollout restart — 새 토큰을 pod env에 반영(미반영 시 스냅샷 401)
#
# EKS(private subnet)에서도 동작 — port-forward는 EKS API 서버 터널을 통한다.
#
# 사용:
#   GRAFANA_ADMIN_PASSWORD=xxx bash setup-grafana-secret.sh
#
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

NAMESPACE="${NAMESPACE:-management}"
GRAFANA_SVC="${GRAFANA_SVC:-grafana}"
LOCAL_PORT="${LOCAL_PORT:-13000}"
ADMIN_USER="${GRAFANA_ADMIN_USER:-admin}"
ADMIN_PASSWORD="${GRAFANA_ADMIN_PASSWORD:-admin}"
# datasource provisioning 파일 (secureJsonData 등 민감정보 포함 — Secret으로만 다룸)
DATASOURCES_FILE="${DATASOURCES_FILE:-${SCRIPT_DIR}/../../provisioning/datasources/datasources.yml}"
BASE="http://127.0.0.1:${LOCAL_PORT}"

log() { echo ">> $*"; }

# 1) datasource 프로비저닝
#    파일의 ${VAR} placeholder를 CI/CD env로 envsubst 치환 → 실제 값을 Secret에 구워 넣고
#    sidecar 라벨 부여. (grafana 파드 env가 비어 있어도 동작 — 치환을 여기서 끝냄.
#     값은 k8s Secret에만 존재하며 git/tfstate/로그에 노출 없음.)
RENDERED=""
log "[1/5] grafana datasource Secret 생성/갱신 (envsubst → sidecar 로드)"
if [ -f "${DATASOURCES_FILE}" ]; then
  # 파일이 참조하는 ${VAR} 들을 추출(변수명만 — 민감 값 아님)하고 env에 모두 있는지 검증
  REQUIRED_VARS=$(grep -oE '\$\{[A-Za-z_][A-Za-z0-9_]*\}' "${DATASOURCES_FILE}" \
    | sed -E 's/[$\{\}]//g' | sort -u)
  MISSING=""
  for v in ${REQUIRED_VARS}; do
    eval "val=\${${v}:-}"
    [ -n "${val}" ] || MISSING="${MISSING} ${v}"
  done
  if [ -n "${MISSING}" ]; then
    echo "ERROR: datasource가 참조하는 env가 비어 있음:${MISSING}"
    echo "       → CI/CD secret으로 주입 후 재실행 (빈 값으로 치환되면 datasource 깨짐)"
    exit 1
  fi
  # 추출한 변수만 치환(그 외 '$'는 보존)
  SUBST_LIST=$(printf '${%s} ' ${REQUIRED_VARS})
  RENDERED="$(mktemp)"
  trap 'rm -f "${RENDERED}"' EXIT  # 실제 creds가 담긴 임시파일 — 종료 시 즉시 삭제
  envsubst "${SUBST_LIST}" < "${DATASOURCES_FILE}" > "${RENDERED}"
  kubectl create secret generic grafana-datasources -n "${NAMESPACE}" \
    --from-file=datasources.yaml="${RENDERED}" \
    --dry-run=client -o yaml | kubectl apply -f -
  kubectl label secret grafana-datasources -n "${NAMESPACE}" grafana_datasource=1 --overwrite
else
  log "    (datasources 파일 없음: ${DATASOURCES_FILE} → datasource provisioning 건너뜀)"
fi

# 2) port-forward + health (Terraform이 띄운 grafana에 붙는다)
log "[2/5] port-forward svc/${GRAFANA_SVC} ${LOCAL_PORT}:3000"
PF_LOG="$(mktemp)"
kubectl port-forward "svc/${GRAFANA_SVC}" "${LOCAL_PORT}:3000" -n "${NAMESPACE}" >"${PF_LOG}" 2>&1 &
PF_PID=$!
trap 'kill "${PF_PID}" 2>/dev/null || true; rm -f "${PF_LOG}" "${RENDERED:-}"' EXIT

log "    Grafana health check"
for _ in $(seq 1 30); do
  curl -sf "${BASE}/api/health" >/dev/null 2>&1 && break
  kill -0 "${PF_PID}" 2>/dev/null || { echo "port-forward 종료됨:"; cat "${PF_LOG}"; exit 1; }
  sleep 2
done
if ! curl -sf "${BASE}/api/health" >/dev/null 2>&1; then
  echo "Grafana 연결 실패 (Terraform 배포 완료/Ready 확인). port-forward 로그:"; cat "${PF_LOG}"
  exit 1
fi

# 3) ServiceAccount 확보 → Editor 권한 보장 → 기존 토큰 정리 → 새 토큰 발급
log "[3/5] management-snapshot SA 확보 및 토큰 발급"
SA_ID=$(curl -s -u "${ADMIN_USER}:${ADMIN_PASSWORD}" \
  "${BASE}/api/serviceaccounts/search?query=management-snapshot" \
  | grep -oE '"id":[0-9]+' | head -1 | grep -oE '[0-9]+' || true)
if [ -z "${SA_ID}" ]; then
  SA_ID=$(curl -s -u "${ADMIN_USER}:${ADMIN_PASSWORD}" -X POST \
    -H "Content-Type: application/json" \
    -d '{"name":"management-snapshot","role":"Editor"}' \
    "${BASE}/api/serviceaccounts" \
    | grep -oE '"id":[0-9]+' | head -1 | grep -oE '[0-9]+' || true)
fi
[ -n "${SA_ID}" ] || { echo "ServiceAccount 확보 실패"; exit 1; }
echo "    SA_ID=${SA_ID}"

# 기존 SA여도 Editor 권한 보장 (스냅샷 생성에는 Editor 필요 — Viewer는 401/403)
curl -s -u "${ADMIN_USER}:${ADMIN_PASSWORD}" -X PATCH \
  -H "Content-Type: application/json" \
  -d '{"role":"Editor"}' \
  "${BASE}/api/serviceaccounts/${SA_ID}" >/dev/null
echo "    role=Editor 보장"

# 기존 토큰 정리 (재발급 시 누적 방지)
for T in $(curl -s -u "${ADMIN_USER}:${ADMIN_PASSWORD}" \
  "${BASE}/api/serviceaccounts/${SA_ID}/tokens" \
  | grep -oE '"id":[0-9]+' | grep -oE '[0-9]+'); do
  echo "    기존 토큰 삭제: ${T}"
  curl -s -u "${ADMIN_USER}:${ADMIN_PASSWORD}" -X DELETE \
    "${BASE}/api/serviceaccounts/${SA_ID}/tokens/${T}" >/dev/null
done

TOKEN=$(curl -s -u "${ADMIN_USER}:${ADMIN_PASSWORD}" -X POST \
  -H "Content-Type: application/json" \
  -d "{\"name\":\"management-token-$(date +%s)\"}" \
  "${BASE}/api/serviceaccounts/${SA_ID}/tokens" \
  | grep -oE '"key":"[^"]+"' | sed -E 's/"key":"([^"]+)"/\1/')
[ -n "${TOKEN}" ] || { echo "토큰 발급 실패 (role/권한 확인)"; exit 1; }

# 4) grafana-secret 생성/갱신
log "[4/5] grafana-secret 생성/갱신"
kubectl create secret generic grafana-secret -n "${NAMESPACE}" \
  --from-literal=token="${TOKEN}" \
  --dry-run=client -o yaml | kubectl apply -f -

# 5) 새 토큰 반영을 위해 management 재시작
#    management는 GRAFANA_TOKEN을 pod 시작 시 env로 주입 → 재시작 전까지 옛 토큰 사용(스냅샷 401).
#    CI/CD로 도는 스크립트이므로 수동 안내가 아니라 직접 rollout restart.
MGMT_NS="${MGMT_NAMESPACE:-${NAMESPACE}}"
log "[5/5] management-service 재시작 (새 토큰 반영)"
if kubectl get deployment/management-service -n "${MGMT_NS}" >/dev/null 2>&1; then
  kubectl rollout restart deployment/management-service -n "${MGMT_NS}"
  echo ">> 완료. management-service 재시작됨 → 새 토큰 반영"
else
  echo ">> 완료. (management-service 배포 없음 → 재시작 생략. 다음 배포 시 새 토큰 자동 반영)"
fi
