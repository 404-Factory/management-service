package com.factory.management.service;

import com.factory.management.event.payload.consumer.SensorViolationPayload;
import com.factory.management.infrastructure.entity.Defect;
import com.factory.management.infrastructure.repository.DefectRepository;
import com.factory.management.simulator.util.DefectGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class DefectServiceIntegrationTest {

    @Autowired
    private DefectService defectService;

    @Autowired
    private DefectRepository defectRepository;

    @MockitoBean
    private DefectGenerator defectGenerator;

    @Test
    @DisplayName("anomaly payload로 Defect가 생성되면 DB에 저장된다")
    void createWithProbability_savesDefectToDb() {
        Instant occurredTime = Instant.parse("2026-06-12T10:00:00Z");
        Defect defect = Defect.builder()
            .defectType("PR")
            .defectCode("PR_THICKNESS")
            .causeEquipmentId(1L)
            .causeEquipmentName("EQ-01")
            .causeProcessId(1L)
            .causeProcessName("DEPOSITION")
            .occurredTime(occurredTime)
            .detectedTime(occurredTime.plusSeconds(600))
            .build();

        SensorViolationPayload payload = new SensorViolationPayload();
        when(defectGenerator.generate(payload)).thenReturn(defect);

        defectService.createWithProbability(payload);

        assertThat(defectRepository.findAll()).hasSize(1);
        Defect saved = defectRepository.findAll().get(0);
        assertThat(saved.getDefectType()).isEqualTo("PR");
        assertThat(saved.getDefectCode()).isEqualTo("PR_THICKNESS");
        assertThat(saved.getCauseEquipmentName()).isEqualTo("EQ-01");
        assertThat(saved.getCauseProcessName()).isEqualTo("DEPOSITION");
    }

    @Test
    @DisplayName("generator가 null을 반환하면 DB에 저장하지 않는다")
    void createWithProbability_doesNotSave_whenGeneratorReturnsNull() {
        SensorViolationPayload payload = new SensorViolationPayload();
        when(defectGenerator.generate(any())).thenReturn(null);

        defectService.createWithProbability(payload);

        assertThat(defectRepository.findAll()).isEmpty();
    }

    @Test
    @DisplayName("여러 Defect를 생성하면 모두 DB에 저장되고 조회된다")
    void createMultipleDefects_allSavedAndQueryable() {
        Instant base = Instant.parse("2026-06-12T10:00:00Z");

        Defect defect1 = Defect.builder()
            .defectType("PR")
            .defectCode("PR_THICKNESS")
            .causeEquipmentName("EQ-01")
            .occurredTime(base)
            .detectedTime(base.plusSeconds(600))
            .build();

        Defect defect2 = Defect.builder()
            .defectType("ETCH_PROFILE")
            .defectCode("ETCH_PROFILE_DEFECT")
            .causeEquipmentName("EQ-01")
            .occurredTime(base.plusSeconds(3600))
            .detectedTime(base.plusSeconds(4200))
            .build();

        SensorViolationPayload payload = new SensorViolationPayload();
        when(defectGenerator.generate(payload))
            .thenReturn(defect1)
            .thenReturn(defect2);

        defectService.createWithProbability(payload);
        defectService.createWithProbability(payload);

        long count = defectRepository.getDefectCount("EQ-01",
            LocalDateTime.of(2026, 6, 12, 0, 0));
        assertThat(count).isEqualTo(2);
    }
}
