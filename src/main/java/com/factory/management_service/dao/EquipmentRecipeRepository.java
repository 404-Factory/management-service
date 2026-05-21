package com.factory.management_service.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.factory.management_service.domain.entity.EquipmentRecipeEntity;

public interface EquipmentRecipeRepository extends JpaRepository<EquipmentRecipeEntity, Long> {
    Optional<EquipmentRecipeEntity> findByEquipment_EquipmentId(Long equipmentId);
}