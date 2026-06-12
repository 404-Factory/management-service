package com.factory.management.event.type;

import com.factory.common.event.domain.EventType;

public enum EquipmentRecipeDetailEventType implements EventType {
    EQUIPMENT_RECIPE_PARAM_UPDATED("EquipmentRecipeParamUpdated");

    private final String name;

    EquipmentRecipeDetailEventType(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }
}
