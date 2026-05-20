package com.factory.management_service.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.factory.management_service.domain.entity.DefectEntity;

import java.util.List;

public interface DefectRepository extends JpaRepository<DefectEntity, Long> {

    @Query("""
                SELECT d.causeEquipmentId, COUNT(d)
                FROM DefectEntity d
                GROUP BY d.causeEquipmentId
            """)
    List<Object[]> countDefectsByEquipment();
}