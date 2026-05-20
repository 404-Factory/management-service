package com.factory.management_service.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.factory.management_service.domain.entity.MasterRecipeDetailEntity;
import com.factory.management_service.domain.entity.MasterRecipeDetailId;

public interface MasterRecipeDetailRepository extends JpaRepository<MasterRecipeDetailEntity, MasterRecipeDetailId> {

}
