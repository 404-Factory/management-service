package com.factory.management.service;

import com.factory.management.event.consume.payload.AnomalyCreatedPayload;
import com.factory.management.infrastructure.entity.Defect;
import com.factory.management.infrastructure.repository.DefectRepository;
import com.factory.management.simulator.util.DefectGenerator;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DefectServiceImpl implements DefectService {

    private final DefectRepository defectRepository;
    private final DefectGenerator defectGenerator;

    @Override
    @Transactional(readOnly = true)
    public long getCount(String equipmentName, LocalDate startDate, LocalDate endDate) {

        return defectRepository.getDefectCount(equipmentName, startDate, endDate);
    }

    @Override
    public void createWithProbability(AnomalyCreatedPayload payload) {
        Defect defect = defectGenerator.generate(payload);
        if (defect != null) {
            defectRepository.save(defect);
        }
    }
}
