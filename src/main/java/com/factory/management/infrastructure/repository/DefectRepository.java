package com.factory.management.infrastructure.repository;

import com.factory.management.infrastructure.entity.Defect;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface DefectRepository extends JpaRepository<Defect, Long> {

    @Query("""
                SELECT d.causeEquipmentId, COUNT(d)
                FROM Defect d
                GROUP BY d.causeEquipmentId
            """)
    List<Object[]> countDefectsByEquipment();

    @Query("""
                SELECT COUNT(d) FROM Defect d
                WHERE d.causeEquipmentName = :equipmentName
                AND d.occurredTime >= :from
            """)
    long countByEquipmentNameSince(
            @Param("equipmentName") String equipmentName,
            @Param("from") LocalDateTime from
    );
}