package com.factory.management.service;

import com.factory.management.dto.response.EquipmentRecipeResponse;
import com.factory.management.exception.ManagementErrorCode;
import com.factory.management.exception.ManagementException;
import com.factory.management.infrastructure.repository.EquipmentRecipeRepository;
import com.factory.management.infrastructure.mapper.EquipmentRecipeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EquipmentRecipeServiceImpl implements EquipmentRecipeService {

    private final EquipmentRecipeRepository equipmentRecipeRepository;
    private final EquipmentRecipeMapper equipmentRecipeMapper;

    @Override
    @Transactional(readOnly = true)
    public EquipmentRecipeResponse getEquipmentRecipeByEquipmentId(Long equipmentId) {

        return equipmentRecipeRepository
            .findTopByEquipment_EquipmentIdOrderByVersionDesc(equipmentId)
            .map(equipmentRecipeMapper::toDto)
            .orElseThrow(
                () -> new ManagementException(ManagementErrorCode.EQUIPMENT_RECIPE_NOT_FOUND)
            );
    }
}
