package com.factory.management.service;

import com.factory.management.event.payload.consumer.SensorViolationPayload;
import com.factory.management.infrastructure.entity.Defect;
import com.factory.management.infrastructure.repository.DefectRepository;
import com.factory.management.simulator.util.DefectGenerator;
import java.time.LocalDateTime;
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
    public long getCount(String equipmentName, LocalDateTime since) {

        return defectRepository.getDefectCount(equipmentName, since);
    }

    @Override
    @Transactional
    public void createWithProbability(SensorViolationPayload payload) {
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
