# Management Service

스마트팩토리 운영 관리 및 이상 이벤트 기반 불량 생성 서비스

---

# Overview

`management-service`는 스마트팩토리 환경에서 다음 역할을 수행하는 백엔드 서비스입니다.

* 공정 / 설비 / Recipe / 이상로그 / 불량 정보 관리 API 제공
* Kafka 기반 이상 이벤트 처리
* 이상로그 기반 확률적 불량 데이터 생성
* Grafana Dashboard 자동 복구 및 초기화 지원

---

# 주요 기능

## 운영 관리 API

다음 기능에 대한 조회 API를 제공합니다.

* 설비 목록 조회
* 설비 상세 조회
* 최근 이상로그 조회
* 설비 적용 Recipe 조회
* Dashboard 설비 상태 조회

---

## 이상 이벤트 기반 불량 생성

Kafka Topic(`anomaly.detected`)을 통해 수신한 이상로그 이벤트를 기반으로
확률적으로 불량 데이터를 생성합니다.

예시:

```text id="3h8qv6"
온도 이상 + 설비 상태 + Recipe 정보
→ defect probability 계산
→ defect 생성
→ defect.created 이벤트 발행
```

테스트 및 시뮬레이션용 가상 불량 데이터 생성에 사용됩니다.

---

## Grafana Dashboard 자동 복구

`grafana-backup` 디렉토리를 통해
Grafana Dashboard / Datasource / 설정을 자동 복구할 수 있습니다.

컨테이너 실행 시 즉시 모니터링 환경을 구성할 수 있습니다.

---

# 기술 스택

* Java 17
* Spring Boot
* Spring Data JPA
* Apache Kafka
* Gradle
* Docker
* Grafana

---

# 프로젝트 구조

```text id="ivvys3"
management-service
├── grafana-backup
├── src
│   ├── main
│   │   ├── java/com/factory/management_service
│   │   │   ├── ctrl
│   │   │   ├── dao
│   │   │   ├── domain
│   │   │   │   ├── dto
│   │   │   │   └── entity
│   │   │   ├── kafka
│   │   │   │   ├── consumer
│   │   │   │   ├── producer
│   │   │   │   └── event
│   │   │   ├── registry
│   │   │   ├── rule
│   │   │   ├── scheduler
│   │   │   └── service
│   │   └── resources
│   └── test
├── Dockerfile
├── build.gradle
└── settings.gradle
```

---

# API

Base URL

```text id="lbkt1o"
/equipments
```

---

# 공통 응답 형식

```json id="wpc7m0"
{
  "success": true,
  "status": 200,
  "message": "success",
  "data": {},
  "timestamp": "2026-05-28T10:11:04.12402"
}
```

---

# 설비 목록 조회

## Request

```http id="kh6g4d"
GET /equipments/list/{processId}
```

## Response

```json id="2afbjw"
{
  "success": true,
  "status": 200,
  "message": "success",
  "data": [
    {
      "equipmentId": 1,
      "equipmentName": "EQP-DEPOSITION-001",
      "processId": 1,
      "processName": "DEPOSITION"
    }
  ],
  "timestamp": "2026-05-28T10:11:04.12402"
}
```

---

# 설비 상세 조회

## Request

```http id="47h4gd"
GET /equipments/{equipmentId}
```

## Response

```json id="u4zj9u"
{
  "success": true,
  "status": 200,
  "message": "success",
  "data": {
    "equipmentId": 1,
    "equipmentName": "EQP-DEPOSITION-001",
    "processId": 1,
    "processName": "DEPOSITION"
  },
  "timestamp": "2026-05-28T10:11:04.12402"
}
```

---

# 최근 이상로그 조회

## Request

```http id="ruw0q6"
GET /equipments/{equipmentId}/anomaly
```

---

# 현재 Recipe 조회

## Request

```http id="qj9uk5"
GET /equipments/{equipmentId}/recipe
```

## Response

```json id="sudv0t"
{
  "success": true,
  "status": 200,
  "message": "success",
  "data": {
    "equipmentRecId": 1,
    "equipmentId": 1,
    "equipmentName": "EQP-DEPOSITION-001",
    "masterRecipeId": 1,
    "version": 1.0,
    "details": [
      {
        "param": "Soft Bake Temperature",
        "min": 85.0,
        "max": 110.0
      },
      {
        "param": "Spin Speed",
        "min": 1500.0,
        "max": 2500.0
      }
    ]
  },
  "timestamp": "2026-05-28T10:10:05.7250808"
}
```

---

# Dashboard 설비 목록 조회

## Request

```http id="7hht37"
GET /equipments/dashboard
```

---

# Kafka Event Architecture

| 송신 서비스             | 수신 서비스               | 설명              | Topic            |
| ------------------ | -------------------- | --------------- | ---------------- |
| management-service | anomaly-service      | 불량 정보 생성 이벤트 발행 | defect.created   |
| anomaly-service    | management-service   | 이상 로그 이벤트 발행    | anomaly.detected |
| anomaly-service    | notification-service | 이상 로그 알림 이벤트 발행 | anomaly.detected |

---

# Kafka Consumer

## anomaly.detected

이상 탐지 이벤트를 수신하여:

* 이상로그 저장
* 확률 기반 불량 생성
* defect.created 이벤트 발행

을 수행합니다.

---

# Kafka Producer

## defect.created

생성된 불량 정보를 Kafka Topic으로 발행합니다.

---

# 실행 방법

## Build

```bash id="ic4r7m"
./gradlew build
```

## Run

```bash id="f6rrtl"
./gradlew bootRun
```

또는

```bash id="59pxga"
docker compose up
```

---

# 환경 설정

```text id="p6iwwz"
application-local.yml
application-dev.yml
application-prod.yml
```

---

