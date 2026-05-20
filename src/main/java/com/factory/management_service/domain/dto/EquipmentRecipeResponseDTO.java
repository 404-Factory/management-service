package com.factory.management_service.domain.dto;

import java.util.List;

import com.factory.management_service.domain.entity.EquipmentRecipeEntity;

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

    private Long equipmentRecId;

    private Long equipmentId;
    private String equipmentName;

    private Long masterRecipeId;

    private Double version;

    private List<EquipmentRecipeDetailResponseDTO> details;

    public static EquipmentRecipeResponseDTO fromEntity(EquipmentRecipeEntity entity) {

        return builder()
                .equipmentRecId(entity.getEquipmentRecId())

                .equipmentId(entity.getEquipment().getEquipmentId())
                .equipmentName(entity.getEquipment().getEquipmentName())

                .masterRecipeId(entity.getMasterRecipe().getMasterRecipeId())

                .version(entity.getVersion())

                .details(
                        entity.getDetails()
                                .stream()
                                .map(EquipmentRecipeDetailResponseDTO::fromEntity)
                                .toList())

                .build();
    }
}