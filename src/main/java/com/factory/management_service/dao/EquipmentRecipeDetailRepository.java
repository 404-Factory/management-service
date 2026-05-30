package com.factory.management_service.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.factory.management_service.domain.entity.EquipmentRecipeDetail;
import com.factory.management_service.domain.entity.EquipmentRecipeDetailId;

public interface EquipmentRecipeDetailRepository extends JpaRepository<EquipmentRecipeDetail, EquipmentRecipeDetailId> {

}
