package com.factory.management.service;

import com.factory.management.infrastructure.repository.DefectRepository;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DefectServiceImpl implements DefectService {

    private final DefectRepository defectRepository;

    @Override
    @Transactional(readOnly = true)
    public long getCount(String equipmentName, LocalDate startDate, LocalDate endDate) {

        return defectRepository.getDefectCount(equipmentName, startDate, endDate);
    }
}
