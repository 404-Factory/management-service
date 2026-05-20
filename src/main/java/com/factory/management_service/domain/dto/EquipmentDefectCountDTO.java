package com.factory.management_service.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EquipmentDefectCountDTO {

    private Long equipmentId;
    private Long defectCount;
}
