package com.factory.management_service.domain.dto;

import com.factory.management_service.domain.entity.EquipmentEntity;
import com.factory.management_service.domain.entity.ProcessEntity;

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
public class EquipmentResponseDTO {
    private Long equipmentId;
    private String equipmentName;
    private ProcessEntity process;

    public static EquipmentResponseDTO fromEntity(EquipmentEntity entity){
        return builder()
            .equipmentId(entity.getEquipmentId())
            .equipmentName(entity.getEquipmentName())
            .process(entity.getProcess())
            .build();
    }
}