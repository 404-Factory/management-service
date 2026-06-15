package com.factory.management.infrastructure.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.factory.management.infrastructure.entity.Snapshot;

public interface SnapshotRepository extends JpaRepository<Snapshot, Long> {
    Optional<Snapshot> findFirstByAnomalyIdOrderByIdDesc(Long anomalyId);
}
