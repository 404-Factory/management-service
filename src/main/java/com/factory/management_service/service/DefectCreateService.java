package com.factory.management_service.service;

import org.springframework.stereotype.Service;

import com.factory.management_service.dao.DefectRepository;
import com.factory.management_service.domain.entity.DefectEntity;
import com.factory.management_service.kafka.event.DefectCreatedEvent;
import com.factory.management_service.kafka.producer.DefectEventProducer;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DefectCreateService {

    private final DefectRepository defectRepository;
    private final DefectEventProducer defectEventProducer;

    public void create(DefectEntity defectEntity) {

        DefectEntity saved = defectRepository.save(defectEntity);

        DefectCreatedEvent event = DefectCreatedEvent.builder()
                .defectId(saved.getDefectId())
                .defectType(saved.getDefectType())
                .defectCode(saved.getDefectCode())
                .detectedTime(saved.getDetectedTime())
                .occurredTime(saved.getOccurredTime())
                .causeProcessId(saved.getCauseProcessId())
                .causeProcessName(saved.getCauseProcessName())
                .causeEquipmentId(saved.getCauseEquipmentId())
                .causeEquipmentName(saved.getCauseEquipmentName())
                .build();

        defectEventProducer.publish(event);
    }
}
