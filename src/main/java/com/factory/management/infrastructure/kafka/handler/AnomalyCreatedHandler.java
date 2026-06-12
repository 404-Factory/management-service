package com.factory.management.infrastructure.kafka.handler;

import com.factory.common.event.domain.Event;
import com.factory.common.inbox.jpa.aop.InboxProcessed;
import com.factory.common.kafka.support.EventHandler;
import com.factory.management.event.payload.consumer.AnomalyCreatedPayload;
import com.factory.management.event.type.AnomalyEventType;
import com.factory.management.service.DefectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class AnomalyCreatedHandler implements EventHandler<AnomalyCreatedPayload> {

    private final DefectService defectService;

    @Override
    public String getEventType() {
        return AnomalyEventType.ANOMALY_CREATED.getName();
    }

    @Override
    @Transactional
    @InboxProcessed
    public void process(Event<AnomalyCreatedPayload> event) {

        defectService.createWithProbability(event.getPayload());
    }
}
