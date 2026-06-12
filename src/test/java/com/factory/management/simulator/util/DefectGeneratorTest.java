package com.factory.management.simulator.util;

import com.factory.management.event.payload.consumer.AnomalyCreatedPayload;
import com.factory.management.infrastructure.entity.Defect;
import com.factory.management.infrastructure.entity.Lot;
import com.factory.management.service.LotService;
import com.factory.management.simulator.registry.RuleRegistry;
import com.factory.management.simulator.rule.DefectCandidate;
import com.factory.management.simulator.type.RuleName;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefectGeneratorTest {

    @Mock
    private LotService lotService;

    @Mock
    private RuleRegistry ruleRegistry;

    @Mock
    private DefectProbabilityUtils defectProbabilityUtils;

    @InjectMocks
    private DefectGenerator defectGenerator;

    private AnomalyCreatedPayload buildPayload() {
        AnomalyCreatedPayload payload = new AnomalyCreatedPayload();
        payload.setEquipmentId(1L);
        payload.setEquipmentName("EQ-01");
        payload.setRecipeParameter("Spin Speed");
        payload.setSeverity("CRITICAL");
        payload.setCauseRule("NELSON_RULE_1");
        payload.setOccurredTime(Instant.parse("2026-06-12T00:00:00Z"));
        return payload;
    }

    @Test
    @DisplayName("확률 조건 미충족 시 null을 반환한다")
    void generate_probabilityFalse_returnsNull() {
        AnomalyCreatedPayload payload = buildPayload();
        when(defectProbabilityUtils.shouldGenerate(payload)).thenReturn(false);

        assertThat(defectGenerator.generate(payload)).isNull();
    }

    @Test
    @DisplayName("매칭되는 rule candidate가 없으면 null을 반환한다")
    void generate_noCandidates_returnsNull() {
        AnomalyCreatedPayload payload = buildPayload();
        when(defectProbabilityUtils.shouldGenerate(payload)).thenReturn(true);
        when(ruleRegistry.getCandidates(any(), any())).thenReturn(List.of());

        assertThat(defectGenerator.generate(payload)).isNull();
    }

    @Test
    @DisplayName("진행 중인 lot이 없으면 null을 반환한다")
    void generate_noLots_returnsNull() {
        AnomalyCreatedPayload payload = buildPayload();
        when(defectProbabilityUtils.shouldGenerate(payload)).thenReturn(true);
        when(ruleRegistry.getCandidates("Spin Speed", RuleName.NELSON_RULE_1))
            .thenReturn(List.of(new DefectCandidate("PR", "PR_THICKNESS", 100)));
        when(lotService.getLotsByEquipmentId(1L)).thenReturn(List.of());

        assertThat(defectGenerator.generate(payload)).isNull();
    }

    @Test
    @DisplayName("유효한 anomaly payload로 Defect를 생성한다")
    void generate_validInput_returnsDefect() {
        AnomalyCreatedPayload payload = buildPayload();
        when(defectProbabilityUtils.shouldGenerate(payload)).thenReturn(true);
        when(ruleRegistry.getCandidates("Spin Speed", RuleName.NELSON_RULE_1))
            .thenReturn(List.of(new DefectCandidate("PR", "PR_THICKNESS", 100)));

        Lot lot = mock(Lot.class);
        when(lotService.getLotsByEquipmentId(1L)).thenReturn(List.of(lot));

        Defect result = defectGenerator.generate(payload);

        assertThat(result).isNotNull();
        assertThat(result.getDefectType()).isEqualTo("PR");
        assertThat(result.getDefectCode()).isEqualTo("PR_THICKNESS");
        assertThat(result.getCauseEquipmentId()).isEqualTo(1L);
        assertThat(result.getCauseEquipmentName()).isEqualTo("EQ-01");
        assertThat(result.getCauseProcessName()).isEqualTo("DEPOSITION");
        assertThat(result.getOccurredTime()).isEqualTo(Instant.parse("2026-06-12T00:00:00Z"));
    }
}
