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
    private long totalOutOfRecipe;
    private List<SensorSummaryDTO> sensors;
}
