package com.factory.management_service.kafka.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.factory.management_service.dao.AnomalyRepository;
import com.factory.management_service.dao.EquipmentRecipeRepository;
import com.factory.management_service.dao.EquipmentRepository;
import com.factory.management_service.domain.entity.AnomalyEntity;
import com.factory.management_service.domain.entity.EquipmentEntity;
import com.factory.management_service.domain.entity.EquipmentRecipeEntity;
import com.factory.management_service.domain.type.AnomalySeverity;
import com.factory.management_service.domain.type.RuleName;
import com.factory.management_service.kafka.event.AnomalyDetectedEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class AnomalyDetectedConsumer {

        private final EquipmentRepository equipmentRepository;
        private final EquipmentRecipeRepository equipmentRecipeRepository;
        private final AnomalyRepository anomalyRepository;

        @KafkaListener(topics = "anomaly.detected", groupId = "management-service")
        @Transactional
        public void consume(AnomalyDetectedEvent event) {

                log.info("consume anomaly event : {}", event);

                EquipmentEntity equipment = equipmentRepository.getReferenceById(
                                event.getEquipmentId());

                EquipmentRecipeEntity recipe = equipmentRecipeRepository.getReferenceById(
                                event.getEquipmentRecipeId());

                AnomalyEntity entity = AnomalyEntity.builder()
                                .equipment(equipment)
                                .equipmentRecipe(recipe)
                                .recipeParameter(event.getRecipeParameter())
                                .severity(AnomalySeverity.valueOf(event.getSeverity()))
                                .occurredTime(event.getOccurredTime())
                                .causeRule(RuleName.valueOf(event.getCauseRule()))
                                .anomalyType(event.getAnomalyType())
                                .windowStartTime(event.getWindowStartTime())
                                .sampleCount(event.getSampleCount())
                                .detectionReason(event.getDetectionReason())
                                .build();

                anomalyRepository.save(entity);
        }
}
