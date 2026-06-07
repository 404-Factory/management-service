package com.factory.management.infrastructure.repository.support;

import com.factory.management.dto.response.DefectCountResponse;
import java.time.LocalDate;

public interface DefectRepositorySupport {
    DefectCountResponse getDefectCount(String equipmentName, LocalDate startDate, LocalDate endDate);
}
