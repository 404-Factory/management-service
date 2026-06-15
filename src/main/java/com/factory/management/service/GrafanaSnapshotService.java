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

    public String createSnapshot(Long anomalyId, String dashboardUid, String from, String to) {

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
