package com.factory.management.infrastructure.repository;

import com.factory.management.infrastructure.entity.MasterRecipeDetail;
import com.factory.management.infrastructure.entity.MasterRecipeDetailId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MasterRecipeDetailRepository extends JpaRepository<MasterRecipeDetail, MasterRecipeDetailId> {

}
