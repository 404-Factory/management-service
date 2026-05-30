package com.factory.management_service.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.factory.management_service.domain.entity.DefectEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface DefectRepository extends JpaRepository<DefectEntity, Long> {

    @Query("""
                SELECT d.causeEquipmentId, COUNT(d)
                FROM DefectEntity d
                GROUP BY d.causeEquipmentId
            """)
    List<Object[]> countDefectsByEquipment();

    @Query("""
                SELECT COUNT(d) FROM DefectEntity d
                WHERE d.causeEquipmentName = :equipmentName
                AND d.occurredTime >= :from
            """)
    long countByEquipmentNameSince(
            @Param("equipmentName") String equipmentName,
            @Param("from") LocalDateTime from
    );
}