package com.factory.management.service;

import org.springframework.stereotype.Service;

import com.factory.management.infrastructure.entity.Snapshot;
import com.factory.management.infrastructure.repository.SnapshotRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SnapshotService {

    private final SnapshotRepository snapshotRepository;

    private static final String DEFAULT_SNAPSHOT_URL = "http://localhost:3000/d/adpp6l5";

    public String getSnapshotUrl(Long anomalyId) {
        return snapshotRepository
                .findByAnomalyId(anomalyId)
                .map(Snapshot::getUrl) // 데이터가 있으면 스냅샷의 URL을 추출
                .orElseGet(() -> DEFAULT_SNAPSHOT_URL); // 없으면 기본 링크 반환
    }
}