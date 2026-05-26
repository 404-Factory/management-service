package com.factory.management_service.dao;

import com.factory.management_service.domain.entity.AlertEntity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AlertRepository extends JpaRepository<AlertEntity, Long> {
    @Query("""
                select a
                from AlertEntity a
                join fetch a.anomalyLog al
                join fetch al.equipment e
                where a.isRead = false
            """)
    List<AlertEntity> findUnreadAlertsWithEquipment();
}