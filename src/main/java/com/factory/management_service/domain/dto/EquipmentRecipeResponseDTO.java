package com.factory.management_service.domain.dto;

import java.util.List;

import com.factory.management_service.domain.entity.EquipmentEntity;
import com.factory.management_service.domain.entity.EquipmentRecipeEntity;
import com.factory.management_service.domain.entity.MasterRecipeEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentRecipeResponseDTO {
    private String equipmentRecId;
    private EquipmentEntity equipment;
    private MasterRecipeEntity masterRecipe;
    private Double version;

    private List<EquipmentRecipeDetailResponseDTO> details;

    public static EquipmentRecipeResponseDTO fromEntity(EquipmentRecipeEntity entity) {
        return builder()
                .equipmentRecId(entity.getEquipmentRecId())
                .equipment(entity.getEquipment())
                .masterRecipe(entity.getMasterRecipe())
                .version(entity.getVersion())
                .details(entity.getDetails()
                        .stream()
                        .map(EquipmentRecipeDetailResponseDTO::fromEntity)
                        .toList())
                .build();
    }
}
