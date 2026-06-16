package com.factory.management.infrastructure.repository.support;

import java.time.LocalDateTime;

public interface DefectRepositorySupport {
    long getDefectCount(String equipmentName, LocalDateTime since);
}
