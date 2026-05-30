package com.factory.management_service.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.factory.management_service.domain.entity.EquipmentEntity;

public interface EquipmentRepository extends JpaRepository<EquipmentEntity, Long> {
    Optional<List<EquipmentEntity>> findByProcess_ProcessId(Long processId);
}