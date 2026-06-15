package com.factory.management.service;

import org.springframework.stereotype.Service;

import com.factory.management.infrastructure.entity.Snapshot;
import com.factory.management.infrastructure.repository.SnapshotRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SnapshotService {

    private final SnapshotRepository snapshotRepository;

    public String getSnapshotUrl(Long anomalyId) {

        Snapshot snapshot = snapshotRepository
                .findByAnomalyId(anomalyId)
                .orElseThrow(() -> new RuntimeException("snapshot 없음"));

        return snapshot.getUrl();
    }
}