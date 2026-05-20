package com.factory.management_service.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.factory.management_service.domain.entity.ProductEntity;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

}