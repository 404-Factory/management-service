package com.factory.management.infrastructure.repository;

import com.factory.management.infrastructure.entity.Equipment;
import com.factory.management.infrastructure.repository.support.EquipmentRepositorySupport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EquipmentRepository extends JpaRepository<Equipment, Long>,
    EquipmentRepositorySupport {
}
