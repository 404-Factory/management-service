package com.factory.management.infrastructure.kafka.handler;

import com.factory.common.event.domain.Event;
import com.factory.common.inbox.jpa.aop.InboxProcessed;
import com.factory.common.kafka.support.EventHandler;
import com.factory.management.event.payload.consumer.SensorViolationPayload;
import com.factory.management.event.type.SensorViolationEventType;
import com.factory.management.service.DefectService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class SensorViolationHandler implements EventHandler<SensorViolationPayload> {

    private final DefectService defectService;

    @Override
    public String getEventType() {
        return SensorViolationEventType.SENSOR_VIOLATION.getName();
    }

    @Override
    @Transactional
    @InboxProcessed
    public void process(Event<SensorViolationPayload> event) {
        defectService.createWithProbability(event.getPayload());
    }
}