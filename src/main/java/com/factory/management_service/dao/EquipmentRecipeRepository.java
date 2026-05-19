package com.factory.management_service.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.factory.management_service.domain.entity.EquipmentRecipeEntity;

public interface EquipmentRecipeRepository extends JpaRepository<EquipmentRecipeEntity, String> {
    Optional<EquipmentRecipeEntity> findByEquipment(Long equipmentId);
}