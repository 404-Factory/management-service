package com.factory.management.infrastructure.repository;

import com.factory.management.infrastructure.entity.EquipmentRecipeDetail;
import com.factory.management.infrastructure.entity.EquipmentRecipeDetailId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EquipmentRecipeDetailRepository extends JpaRepository<EquipmentRecipeDetail, EquipmentRecipeDetailId> {

}
