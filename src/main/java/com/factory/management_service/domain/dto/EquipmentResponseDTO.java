package com.factory.management_service.domain.dto;

import com.factory.management_service.domain.entity.EquipmentEntity;
import com.factory.management_service.domain.type.EquipmentStatus;

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

    private Long processId;
    private String processName;

    private EquipmentStatus status;

    public static EquipmentResponseDTO fromEntity(EquipmentEntity entity) {
        return builder()
                .equipmentId(entity.getEquipmentId())
                .equipmentName(entity.getEquipmentName())

                .processId(entity.getProcess().getProcessId())
                .processName(entity.getProcess().getProcessName())

                .status(entity.getStatus())

                .build();
    }
}