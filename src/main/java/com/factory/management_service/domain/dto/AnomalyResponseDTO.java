package com.factory.management_service.domain.dto;

import java.time.LocalDateTime;

import com.factory.management_service.domain.entity.AnomalyEntity;
import com.factory.management_service.domain.entity.EquipmentEntity;
import com.factory.management_service.domain.entity.EquipmentRecipeEntity;
import com.factory.management_service.domain.entity.Severity;

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
public class AnomalyResponseDTO {
    private Long logId;
    private EquipmentEntity equipment;
    private EquipmentRecipeEntity equipmentRecipe;
    private String recipeParameter;
    private Severity severity;
    private LocalDateTime occurredTime;
    private Integer causeRule;

    public static AnomalyResponseDTO fromEntity(AnomalyEntity entity){
        return builder()
            .logId(entity.getLogId())
            .equipment(entity.getEquipment())
            .equipmentRecipe(entity.getEquipmentRecipe())
            .recipeParameter(entity.getRecipeParameter())
            .severity(entity.getSeverity())
            .occurredTime(entity.getOccurredTime())
            .causeRule(entity.getCauseRule())
            .build();
    }
}
