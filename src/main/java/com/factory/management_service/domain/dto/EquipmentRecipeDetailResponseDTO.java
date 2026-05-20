package com.factory.management_service.domain.dto;

import com.factory.management_service.domain.entity.EquipmentRecipeDetail;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentRecipeDetailResponseDTO {

    private String param;
    private Double min;
    private Double max;

    public static EquipmentRecipeDetailResponseDTO fromEntity(EquipmentRecipeDetail entity) {
        return EquipmentRecipeDetailResponseDTO.builder()
                .param(entity.getId().getParam())
                .min(entity.getMin())
                .max(entity.getMax())
                .build();
    }
}