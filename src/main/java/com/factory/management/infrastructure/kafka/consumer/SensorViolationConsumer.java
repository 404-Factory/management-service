package com.factory.management.infrastructure.kafka.consumer;

import com.factory.management.event.payload.consumer.SensorViolationPayload;
import com.factory.management.service.DefectService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SensorViolationConsumer {

    private final ObjectMapper objectMapper;
    private final DefectService defectService;

    @KafkaListener(topics = "${app.kafka.consumer.violation-topic:sensor-violations}", groupId = "${app.kafka.consumer.violation-group-id:anomaly-violation-group}")
    public void consume(String message) {
        log.info("Received sensor violation message: {}", message);
        try {
            JsonNode rootNode = objectMapper.readTree(message);
            SensorViolationPayload payload;
            if (rootNode != null && rootNode.has("payload")) {
                payload = objectMapper.treeToValue(rootNode.get("payload"), SensorViolationPayload.class);
            } else {
                payload = objectMapper.readValue(message, SensorViolationPayload.class);
            }
            defectService.createWithProbability(payload);
        } catch (Exception e) {
            log.error("Error processing sensor violation message: {}", message, e);
        }
    }
}