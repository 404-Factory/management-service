package com.factory.management.infrastructure.repository;

import com.factory.management.infrastructure.entity.MasterRecipe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MasterRecipeRepository extends JpaRepository<MasterRecipe, Long> {

}
