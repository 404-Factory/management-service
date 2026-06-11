package com.factory.management_service.service;

import com.factory.common.event.support.DomainEventFactory;
import com.factory.management_service.dao.DefectRepository;
import com.factory.management_service.domain.entity.DefectEntity;
import com.factory.management_service.kafka.dto.DefectCreatedPayload;
import java.time.ZoneOffset;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DefectCreateService {

    private final DefectRepository defectRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final DomainEventFactory domainEventFactory;

    @Transactional
    public void create(DefectEntity defectEntity) {
        DefectEntity saved = defectRepository.save(defectEntity);

        DefectCreatedPayload payload = DefectCreatedPayload.builder()
            .defectId(saved.getDefectId())
            .lotId(saved.getLot() != null ? saved.getLot().getLotId() : null)
            .defectType(saved.getDefectType())
            .defectCode(saved.getDefectCode())
            .detectedTime(saved.getDetectedTime() != null
                ? saved.getDetectedTime().toInstant(ZoneOffset.UTC) : null)
            .occurredTime(saved.getOccurredTime() != null
                ? saved.getOccurredTime().toInstant(ZoneOffset.UTC) : null)
            .causeProcessId(saved.getCauseProcessId())
            .causeProcessName(saved.getCauseProcessName())
            .causeEquipmentId(saved.getCauseEquipmentId())
            .causeEquipmentName(saved.getCauseEquipmentName())
            .build();

        eventPublisher.publishEvent(
            domainEventFactory.create(
                () -> "DefectCreated",
                "Defect",
                saved.getDefectId().toString(),
                payload
            )
        );
    }
}
