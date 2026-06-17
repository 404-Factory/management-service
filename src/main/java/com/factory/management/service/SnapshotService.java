package com.factory.management.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.factory.management.infrastructure.entity.Snapshot;
import com.factory.management.infrastructure.repository.SnapshotRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SnapshotService {

    private final SnapshotRepository snapshotRepository;

    // 스냅샷이 아직 없을 때 반환할 라이브 패널(d-solo) 기본 URL의 grafana base.
    // 브라우저가 접근하는 공개 주소여야 함(localhost 아님). env GRAFANA_PUBLIC_URL 로 주입, 기본은 sigma 도메인.
    @Value("${grafana.public-url:https://sigma.mhpark0605.shop/grafana}")
    private String grafanaPublicUrl;

    public String getSnapshotUrl(Long anomalyId) {
        return snapshotRepository
                .findFirstByAnomalyIdOrderByIdDesc(anomalyId)
                .map(Snapshot::getUrl) // 데이터가 있으면 스냅샷의 URL을 추출
                .orElseGet(() -> grafanaPublicUrl + "/d-solo/adpp6l5/anomalydetail"); // 없으면 라이브 패널 기본 링크
    }
}