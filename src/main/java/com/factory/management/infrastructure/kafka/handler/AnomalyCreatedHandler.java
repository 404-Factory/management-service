package com.factory.management.infrastructure.kafka.handler;

import com.factory.common.event.domain.Event;
import com.factory.common.inbox.jpa.aop.InboxProcessed;
import com.factory.common.kafka.support.EventHandler;
import com.factory.management.event.payload.consumer.AnomalyCreatedPayload;
import com.factory.management.event.type.AnomalyEventType;
import com.factory.management.service.DefectService;
import com.factory.management.service.GrafanaSnapshotService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class AnomalyCreatedHandler implements EventHandler<AnomalyCreatedPayload> {

    private final DefectService defectService;
    private final GrafanaSnapshotService grafanaSnapshotService;
    private final String dashboardUid = "adpp6l5";

    @Override
    public String getEventType() {
        return AnomalyEventType.ANOMALY_CREATED.getName();
    }

    @Override
    @Transactional
    @InboxProcessed
    public void process(Event<AnomalyCreatedPayload> event) {
        AnomalyCreatedPayload payload = event.getPayload();
        log.info("[AnomalyCreated] equipmentId={}, severity={}, causeRule={}, recipeParameter={}",
            payload.getEquipmentId(), payload.getSeverity(), payload.getCauseRule(), payload.getRecipeParameter());

        Instant occurredTime = event.getPayload().getOccurredTime();

        Instant fromInstant = occurredTime.minus(30, ChronoUnit.MINUTES);
        Instant toInstant = occurredTime.plus(30, ChronoUnit.MINUTES);

        String fromStr = fromInstant.toString();
        String toStr = toInstant.toString();

        defectService.createWithProbability(event.getPayload());
        grafanaSnapshotService.createSnapshot(event.getPayload().getAnomalyId(), dashboardUid, fromStr, toStr,
                event.getPayload().getEquipmentName());
    }
}
