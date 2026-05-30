package com.factory.management_service.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MonthlySummaryResponseDTO {
    private long totalDefects;
    private long totalAnomalies;
    private List<SensorSummaryDTO> sensors;
}
