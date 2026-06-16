package com.factory.management.event.consume.handler;

import com.factory.common.event.domain.Event;
import com.factory.common.inbox.jpa.aop.InboxProcessed;
import com.factory.common.kafka.support.EventHandler;
import com.factory.management.event.consume.payload.AnomalyCreatedPayload;
import com.factory.management.event.consume.payload.SensorViolationPayload;
import com.factory.management.event.type.AnomalyEventType;
import com.factory.management.event.type.SensorViolationEventType;
import com.factory.management.service.DefectService;
import com.factory.management.service.GrafanaSnapshotService;

import lombok.RequiredArgsConstructor;

import java.time.Instant;

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
