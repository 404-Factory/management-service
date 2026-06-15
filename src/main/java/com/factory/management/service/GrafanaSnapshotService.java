package com.factory.management.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.factory.management.dto.request.SnapshotRequest;
import com.factory.management.dto.response.DashboardResponse;
import com.factory.management.dto.response.SnapshotResponse;
import com.factory.management.infrastructure.entity.Snapshot;
import com.factory.management.infrastructure.repository.SnapshotRepository;

import lombok.RequiredArgsConstructor;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class GrafanaSnapshotService {

    private final RestClient restClient = RestClient.builder().build();

    private final SnapshotRepository snapshotRepository;

    @Value("${grafana.url}")
    private String grafanaUrl;

    @Value("${grafana.token}")
    private String grafanaToken;

    public String createSnapshot(Long anomalyId, String dashboardUid, String from, String to, String equipmentName) {

        // 1. 원본 대시보드 JSON 템플릿(패널 구조) 가져오기
        DashboardResponse originalDashboard = restClient.get()
                .uri(grafanaUrl + "/api/dashboards/uid/" + dashboardUid)
                .header("Authorization", "Bearer " + grafanaToken)
                .retrieve()
                .body(DashboardResponse.class);

        if (originalDashboard == null || originalDashboard.dashboard() == null) {
            throw new RuntimeException("그라파나 원본 대시보드를 불러오지 못했습니다.");
        }

        // 가져온 대시보드 객체 추출
        Map<String, Object> dashboardMap = originalDashboard.dashboard();

        // 대시보드 변수(templating) 중에서 "equipment" 변수의 현재 선택값과 쿼리값을 요청받은 장비명으로 변경
        if (dashboardMap.containsKey("templating")) {
            Map<String, Object> templating = (Map<String, Object>) dashboardMap.get("templating");
            if (templating != null && templating.containsKey("list")) {
                java.util.List<Map<String, Object>> variableList = (java.util.List<Map<String, Object>>) templating
                        .get("list");

                if (variableList != null) {
                    for (Map<String, Object> variable : variableList) {
                        // 대시보드에 설정된 변수명이 "equipment"인 것을 탐색
                        if ("equipment".equals(variable.get("name"))) {
                            // 현재 선택 값(current)과 기본 값(query)을 모두 요청받은 장비명으로 변경
                            variable.put("query", equipmentName);

                            Map<String, Object> current = new java.util.HashMap<>();
                            current.put("selected", true);
                            current.put("text", equipmentName);
                            current.put("value", equipmentName);
                            variable.put("current", current);
                            break;
                        }
                    }
                }
            }
        }

        Map<String, String> forcedTimeRange = Map.of(
                "from", from,
                "to", to);
        dashboardMap.put("time", forcedTimeRange);

        // 2. 스냅샷 생성 요청 바디 구성 (expires: 0 = 영구 보존)
        SnapshotRequest snapshotRequest = new SnapshotRequest(
                dashboardMap,
                0,
                false);

        // 3. 스냅샷 생성 API 호출
        SnapshotResponse snapshotResponse = restClient.post()
                .uri(grafanaUrl + "/api/snapshots")
                .header("Authorization", "Bearer " + grafanaToken)
                .header("Content-Type", "application/json")
                .body(snapshotRequest)
                .retrieve()
                .body(SnapshotResponse.class);

        if (snapshotResponse == null) {
            throw new RuntimeException("그라파나 스냅샷 생성에 실패했습니다.");
        }

        // 4. 스냅샷 정보 저장
        Snapshot snapshot = Snapshot.builder()
                .anomalyId(anomalyId)
                .url(snapshotResponse.url())
                .build();

        snapshotRepository.save(snapshot);

        return snapshotResponse.url();
    }
}
