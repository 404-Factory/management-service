package com.factory.management_service.domain.dto;

import lombok.*;

@Getter
@Builder
public class AlertEquipmentResponseDTO {

    private Long alertId;
    private Long equipmentId;
    private String severity;
}