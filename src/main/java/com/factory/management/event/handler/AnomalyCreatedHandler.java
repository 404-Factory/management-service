package com.factory.management.event.handler;

import com.factory.common.event.domain.Event;
import com.factory.common.inbox.jpa.aop.InboxProcessed;
import com.factory.common.kafka.support.EventHandler;
import com.factory.management.event.payload.AnomalyCreatedPayload;
import com.factory.management.event.payload.type.AnomalyEventType;
import com.factory.management.infrastructure.entity.Defect;
import com.factory.management.service.DefectService;
import com.factory.management.simulator.util.DefectGenerator;
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
