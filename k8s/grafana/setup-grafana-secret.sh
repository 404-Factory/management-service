#!/usr/bin/env bash
#
# Grafana 배포 + 서비스계정 토큰 발급(grafana-secret)을 러너에서 수행한다.
# GitHub 러너(또는 kubectl/aws/curl 갖춘 로컬)에서 한 번 돌리면 끝 — 인클러스터 Job/이미지 불필요.
#
# 흐름:
#   grafana-admin(부트스트랩 자격증명) → grafana 배포 → port-forward
#   → management-snapshot SA 확보 → 기존 토큰 정리 → 새 토큰 발급 → grafana-secret 생성
#
# EKS(private subnet)에서도 동작 — port-forward는 EKS API 서버 터널을 통하므로 직접 접근 불필요.
#
# 사용:
#   bash setup-grafana-secret.sh
#   GRAFANA_ADMIN_PASSWORD=xxx bash setup-grafana-secret.sh   # admin 비번 지정
#
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

NAMESPACE="${NAMESPACE:-management}"
GRAFANA_SVC="${GRAFANA_SVC:-grafana}"
LOCAL_PORT="${LOCAL_PORT:-13000}"
ADMIN_USER="${GRAFANA_ADMIN_USER:-admin}"
ADMIN_PASSWORD="${GRAFANA_ADMIN_PASSWORD:-admin}"
BASE="http://127.0.0.1:${LOCAL_PORT}"

log() { echo ">> $*"; }

# 1) 부트스트랩 admin 자격증명 secret (grafana 기동에 사용)
log "[1/5] grafana-admin secret 생성/갱신"
kubectl create secret generic grafana-admin -n "${NAMESPACE}" \
  --from-literal=admin_user="${ADMIN_USER}" \
  --from-literal=admin_password="${ADMIN_PASSWORD}" \
  --dry-run=client -o yaml | kubectl apply -f -

# 2) Grafana 배포 + 기동 대기
log "[2/5] grafana.yml 적용 및 기동 대기"
kubectl apply -f "${SCRIPT_DIR}/grafana.yml"
kubectl rollout status "deployment/${GRAFANA_SVC}" -n "${NAMESPACE}" --timeout=180s

# 3) port-forward (백그라운드, 스크립트 종료 시 정리)
log "[3/5] port-forward svc/${GRAFANA_SVC} ${LOCAL_PORT}:3000"
kubectl port-forward "svc/${GRAFANA_SVC}" "${LOCAL_PORT}:3000" -n "${NAMESPACE}" >/dev/null 2>&1 &
PF_PID=$!
trap 'kill "${PF_PID}" 2>/dev/null || true' EXIT

log "    Grafana health check"
for _ in $(seq 1 30); do
  curl -sf "${BASE}/api/health" >/dev/null 2>&1 && break
  sleep 2
done
curl -sf "${BASE}/api/health" >/dev/null 2>&1 || { echo "Grafana 연결 실패"; exit 1; }

# 4) ServiceAccount 확보 → 기존 토큰 정리 → 새 토큰 발급
log "[4/5] management-snapshot SA 확보 및 토큰 발급"
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

# 기존 토큰 정리 (재발급 시 누적 방지 — CronJob 대체)
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

# 5) grafana-secret 생성/갱신
log "[5/5] grafana-secret 생성/갱신"
kubectl create secret generic grafana-secret -n "${NAMESPACE}" \
  --from-literal=token="${TOKEN}" \
  --dry-run=client -o yaml | kubectl apply -f -

echo ">> 완료. management에 토큰 반영:"
echo "   kubectl rollout restart deployment/management-service -n ${NAMESPACE}"
