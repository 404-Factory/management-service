package com.factory.management.infrastructure.kafka.handler;

import com.factory.common.event.domain.Event;
import com.factory.common.inbox.jpa.aop.InboxProcessed;
import com.factory.common.kafka.support.EventHandler;
import com.factory.management.event.payload.consumer.AnomalyCreatedPayload;
import com.factory.management.event.type.AnomalyEventType;
import com.factory.management.service.GrafanaSnapshotService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class AnomalyCreatedHandler implements EventHandler<AnomalyCreatedPayload> {

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

        // Grafana 스냅샷은 best-effort — Grafana 장애/누락 필드로 실패해도 이벤트 처리는 계속한다.
        try {
            String fromStr = payload.getFirstDetectedAt().toString();
            String toStr = payload.getLastDetectedAt().toString();
            grafanaSnapshotService.createSnapshot(payload.getAnomalyId(), dashboardUid, fromStr, toStr,
                    payload.getEquipmentName());
        } catch (Exception e) {
            log.error("Failed to create Grafana snapshot. anomalyId={}", payload.getAnomalyId(), e);
        }
    }
}