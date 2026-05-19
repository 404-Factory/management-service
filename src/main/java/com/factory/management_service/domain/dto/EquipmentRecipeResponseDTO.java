package com.factory.management_service.domain.dto;


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

    public static EquipmentRecipeResponseDTO fromEntity(EquipmentRecipeEntity entity){
        return builder()
            .equipmentRecId(entity.getEquipmentRecId())
            .equipment(entity.getEquipment())
            .masterRecipe(entity.getMasterRecipe())
            .version(entity.getVersion())
            .build();
    }
}
