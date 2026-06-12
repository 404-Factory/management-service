package com.factory.management.service;

import com.factory.management.dto.response.EquipmentRecipeResponse;

public interface EquipmentRecipeService {

    EquipmentRecipeResponse getEquipmentRecipeByEquipmentId(Long equipmentId);
}
