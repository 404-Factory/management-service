package com.factory.management.infrastructure.repository.support;

import java.time.LocalDate;

public interface DefectRepositorySupport {
    long getDefectCount(String equipmentName, LocalDate startDate, LocalDate endDate);
}
