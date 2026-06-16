package com.factory.management.infrastructure.kafka;

import com.factory.management.event.payload.consumer.SensorViolationPayload;
import com.factory.management.service.DefectService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Flink가 발행하는 sensor violation을 직접 소비해 defect 생성으로 위임한다.
 *
 * <p>도메인 이벤트(envelope/eventType 라우팅)와 달리 Flink violation은 별도 포맷·토픽이라
 * {@code EventDispatcher}(CommonKafkaConsumer)를 거치지 않고 전용 컨슈머로 받는다.
 * exactly-once 대응을 위해 {@code flinkKafkaListenerContainerFactory}(read_committed)를 쓴다.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SensorViolationConsumer {

    private final ObjectMapper objectMapper;
    private final DefectService defectService;

    @KafkaListener(
        topics = "${app.kafka.consumer.violation-topic:sensor-violations}",
        groupId = "${app.kafka.consumer.violation-group-id:management-violation-group}",
        containerFactory = "flinkKafkaListenerContainerFactory"
    )
    public void consume(String message) {
        log.info("Received sensor violation message from Kafka: {}", message);
        try {
            JsonNode rootNode = objectMapper.readTree(message);
            SensorViolationPayload violation;
            if (rootNode != null && rootNode.has("payload")) {
                violation = objectMapper.treeToValue(rootNode.get("payload"), SensorViolationPayload.class);
            } else {
                violation = objectMapper.readValue(message, SensorViolationPayload.class);
            }
            defectService.createWithProbability(violation);
        } catch (Exception e) {
            log.error("Error processing Kafka sensor violation message: {}", message, e);
        }
    }
}
