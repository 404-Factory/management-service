package com.factory.management_service.kafka.producer;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.factory.management_service.kafka.event.DefectCreatedEvent;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DefectEventProducer {
    private final KafkaTemplate<String, DefectCreatedEvent> kafkaTemplate;

    private static final String TOPIC = "management-event";

    public void publish(DefectCreatedEvent event) {

        kafkaTemplate.send(TOPIC, event.getDefectId().toString(), event);
    }
}
