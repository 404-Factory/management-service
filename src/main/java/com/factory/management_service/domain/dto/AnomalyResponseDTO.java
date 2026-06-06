package com.factory.management_service.domain.dto;

import java.time.LocalDateTime;

import com.factory.management_service.domain.entity.AnomalyEntity;
import com.factory.management_service.domain.type.AnomalySeverity;
import com.factory.management_service.domain.type.RuleName;

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

    private Long equipmentId;
    private String equipmentName;

    private Long equipmentRecipeId;

    private String recipeParameter;
    private AnomalySeverity severity;
    private LocalDateTime occurredTime;
    private RuleName causeRule;

    public static AnomalyResponseDTO fromEntity(AnomalyEntity entity) {
        return builder()
                .logId(entity.getLogId())

                .equipmentId(entity.getEquipment().getEquipmentId())
                .equipmentName(entity.getEquipment().getEquipmentName())

                .equipmentRecipeId(entity.getEquipmentRecipe().getEquipmentRecId())

                .recipeParameter(entity.getRecipeParameter())
                .severity(entity.getSeverity())
                .occurredTime(entity.getOccurredTime())
                .causeRule(entity.getCauseRule())
                .build();
    }
}