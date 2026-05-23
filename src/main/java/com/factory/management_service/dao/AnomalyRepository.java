package com.factory.management_service.dao;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.factory.management_service.domain.entity.AnomalyEntity;

public interface AnomalyRepository extends JpaRepository<AnomalyEntity, Long> {
    /**
     * 최근 anomaly 조회
     */
    @Query("""
                SELECT a
                FROM AnomalyEntity a
                WHERE a.occurredTime >= :from
                ORDER BY a.occurredTime DESC
            """)
    List<AnomalyEntity> findRecentAnomalies(
            LocalDateTime from);
}
