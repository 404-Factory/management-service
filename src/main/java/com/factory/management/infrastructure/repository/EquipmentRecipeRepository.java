package com.factory.management.infrastructure.repository;

import com.factory.management.infrastructure.entity.EquipmentRecipe;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EquipmentRecipeRepository extends JpaRepository<EquipmentRecipe, Long> {

    @EntityGraph(attributePaths = "details")
    Optional<EquipmentRecipe> findTopByEquipment_EquipmentIdOrderByVersionDesc(
        Long equipmentId);
}
