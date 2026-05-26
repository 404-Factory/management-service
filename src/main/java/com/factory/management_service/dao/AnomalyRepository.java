package com.factory.management_service.dao;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.factory.management_service.domain.entity.AnomalyEntity;

public interface AnomalyRepository extends JpaRepository<AnomalyEntity, Long> {
    /**
     * 최근 anomaly 조회
     */
    @Query("""
                SELECT a
                FROM AnomalyEntity a
                join fetch a.equipment e
                WHERE a.occurredTime >= :from
                ORDER BY a.occurredTime DESC
            """)
    List<AnomalyEntity> findRecentAnomalies(
            @Param("from") LocalDateTime from);

    @Query("""
                SELECT a
                FROM AnomalyEntity a
                join fetch a.equipment e
                WHERE a.occurredTime >= :from
                AND e.equipmentId = :equipmentId
                ORDER BY a.occurredTime DESC
            """)
    List<AnomalyEntity> findRecentAnomaliesByEquipmentId(
            @Param("from") LocalDateTime from, @Param("equipmentId") Long equipmentId);
}
