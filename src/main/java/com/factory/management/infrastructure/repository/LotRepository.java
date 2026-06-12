package com.factory.management.infrastructure.repository;

import com.factory.management.infrastructure.entity.Lot;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LotRepository extends JpaRepository<Lot, Long> {
    List<Lot> findByEquipmentId(Long equipmentId);
}
