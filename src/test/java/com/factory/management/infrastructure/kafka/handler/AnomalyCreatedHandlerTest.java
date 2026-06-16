package com.factory.management.infrastructure.kafka.handler;

import com.factory.common.event.domain.Event;
import com.factory.management.event.payload.consumer.AnomalyCreatedPayload;
import com.factory.management.service.GrafanaSnapshotService;
import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AnomalyCreatedHandlerTest {

    @Mock
    private GrafanaSnapshotService grafanaSnapshotService;

    @InjectMocks
    private AnomalyCreatedHandler handler;

    @Test
    @DisplayName("getEventType()이 AnomalyCreated를 반환한다")
    void getEventType_returnsAnomalyCreated() {
        assertThat(handler.getEventType()).isEqualTo("AnomalyCreated");
    }

    @Test
    @DisplayName("process() 호출 시 firstDetectedAt~lastDetectedAt 구간으로 grafana 스냅샷을 생성한다")
    @SuppressWarnings("unchecked")
    void process_createsGrafanaSnapshot() {
        Instant from = Instant.parse("2026-06-11T04:00:00Z");
        Instant to = Instant.parse("2026-06-11T04:30:00Z");

        AnomalyCreatedPayload payload = new AnomalyCreatedPayload();
        payload.setAnomalyId(10L);
        payload.setEquipmentId(1L);
        payload.setEquipmentName("EQ-01");
        payload.setSeverity("CRITICAL");
        payload.setFirstDetectedAt(from);
        payload.setLastDetectedAt(to);

        Event<AnomalyCreatedPayload> event = mock(Event.class);
        when(event.getPayload()).thenReturn(payload);

        handler.process(event);

        verify(grafanaSnapshotService).createSnapshot(10L, "adpp6l5", from.toString(), to.toString(), "EQ-01");
    }
}
