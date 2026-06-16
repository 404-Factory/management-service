package com.factory.management.infrastructure.kafka.handler;

import com.factory.common.event.domain.Event;
import com.factory.management.event.payload.consumer.AnomalyCreatedPayload;
import com.factory.management.service.DefectService;
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
    private DefectService defectService;

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
    @DisplayName("process() 호출 시 payload를 defectService에 전달한다")
    @SuppressWarnings("unchecked")
    void process_delegatesToDefectService() {
        AnomalyCreatedPayload payload = new AnomalyCreatedPayload();
        payload.setEquipmentId(1L);
        payload.setSeverity("CRITICAL");
        payload.setFirstDetectedAt(Instant.parse("2026-06-11T04:00:00Z"));
        payload.setLastDetectedAt(Instant.parse("2026-06-11T04:30:00Z"));

        Event<AnomalyCreatedPayload> event = mock(Event.class);
        when(event.getPayload()).thenReturn(payload);

        handler.process(event);

        verify(defectService).createWithProbability(payload);
    }
}
