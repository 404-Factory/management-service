package com.factory.management_service.kafka.consumer;

import com.factory.common.event.domain.EventEnvelope;
import com.factory.common.event.payload.AnomalyDetectedEventPayload;
import com.factory.management_service.domain.entity.DefectEntity;
import com.factory.management_service.service.DefectCreateService;
import com.factory.management_service.service.DefectGeneratorService;
import com.factory.management_service.service.DefectProbabilityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AnomalyEventConsumer {

    private final DefectProbabilityService probabilityService;
    private final DefectGeneratorService generatorService;
    private final DefectCreateService createService;

    @KafkaListener(topics = "anomaly-event", groupId = "management-service")
    public void consume(EventEnvelope<AnomalyDetectedEventPayload> envelope) {
        log.info("consume anomaly-event: {}", envelope);
        if (envelope == null || envelope.getPayload() == null) {
            return;
        }

        AnomalyDetectedEventPayload payload = envelope.getPayload();
        try {
            boolean shouldGenerate = probabilityService.shouldGenerate(payload);
            if (!shouldGenerate) {
                log.info("Defect generation skipped by probability check for equipmentId={}", payload.getEquipmentId());
                return;
            }

            DefectEntity defectInfo = generatorService.generate(payload);
            if (defectInfo == null) {
                return;
            }

            createService.create(defectInfo);
            log.info("[DEFECT GENERATED FROM KAFKA] type={}, code={}", defectInfo.getDefectType(), defectInfo.getDefectCode());
        } catch (Exception e) {
            log.error("Failed to generate defect from anomaly event envelope: {}", envelope, e);
        }
    }
}
