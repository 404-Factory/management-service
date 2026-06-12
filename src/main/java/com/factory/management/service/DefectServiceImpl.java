package com.factory.management.service;

import com.factory.management.event.payload.consumer.AnomalyCreatedPayload;
import com.factory.management.infrastructure.entity.Defect;
import com.factory.management.infrastructure.repository.DefectRepository;
import com.factory.management.simulator.util.DefectGenerator;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
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
            log.info("[DefectCreated] defectType={}, defectCode={}, equipmentName={}",
                defect.getDefectType(), defect.getDefectCode(), defect.getCauseEquipmentName());
            defectRepository.save(defect);
        } else {
            log.debug("[DefectSkipped] equipmentId={}, severity={}",
                payload.getEquipmentId(), payload.getSeverity());
        }
    }
}
