package com.factory.management.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class EquipmentRecipeDetailResponse {

    private String param;
    private Double min;
    private Double max;

    @Builder
    public EquipmentRecipeDetailResponse(String param, Double min, Double max) {
        this.param = param;
        this.min = min;
        this.max = max;
    }
}
