package com.factory.management_service.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.factory.management_service.domain.entity.MasterRecipeEntity;

public interface MasterRecipeRepository extends JpaRepository<MasterRecipeEntity, Long> {

}
