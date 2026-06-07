package com.factory.management.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SensorSummaryDTO {
    private String sensorType;
    private String unit;
    private double avgValue;
}
