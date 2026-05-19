package com.factory.management_service.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.factory.management_service.domain.entity.EquipmentEntity;

public interface EquipmentRepository extends JpaRepository<EquipmentEntity, Long> {
    
}