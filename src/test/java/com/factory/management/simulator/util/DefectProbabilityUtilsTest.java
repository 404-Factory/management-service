package com.factory.management.simulator.util;

import com.factory.management.event.payload.consumer.AnomalyCreatedPayload;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefectProbabilityUtilsTest {

    private DefectProbabilityUtils defectProbabilityUtils;

    @Mock
    private Random mockRandom;

    @BeforeEach
    void setUp() {
        defectProbabilityUtils = new DefectProbabilityUtils();
        ReflectionTestUtils.setField(defectProbabilityUtils, "random", mockRandom);
    }

    @Test
    @DisplayName("null payload이면 DEFAULT_RATE(10%)가 적용된다")
    void nullPayload_usesDefaultRate() {
        when(mockRandom.nextInt(100)).thenReturn(9);
        assertThat(defectProbabilityUtils.shouldGenerate(null)).isTrue();

        when(mockRandom.nextInt(100)).thenReturn(10);
        assertThat(defectProbabilityUtils.shouldGenerate(null)).isFalse();
    }

    @Test
    @DisplayName("severity가 null이면 DEFAULT_RATE(10%)가 적용된다")
    void nullSeverity_usesDefaultRate() {
        AnomalyCreatedPayload payload = new AnomalyCreatedPayload();

        when(mockRandom.nextInt(100)).thenReturn(9);
        assertThat(defectProbabilityUtils.shouldGenerate(payload)).isTrue();

        when(mockRandom.nextInt(100)).thenReturn(10);
        assertThat(defectProbabilityUtils.shouldGenerate(payload)).isFalse();
    }

    @Test
    @DisplayName("CAUTION severity이면 CAUTION_RATE(15%)가 적용된다")
    void cautionSeverity_usesCautionRate() {
        AnomalyCreatedPayload payload = new AnomalyCreatedPayload();
        payload.setSeverity("CAUTION");

        when(mockRandom.nextInt(100)).thenReturn(14);
        assertThat(defectProbabilityUtils.shouldGenerate(payload)).isTrue();

        when(mockRandom.nextInt(100)).thenReturn(15);
        assertThat(defectProbabilityUtils.shouldGenerate(payload)).isFalse();
    }

    @Test
    @DisplayName("CRITICAL severity이면 CRITICAL_RATE(50%)가 적용된다")
    void criticalSeverity_usesCriticalRate() {
        AnomalyCreatedPayload payload = new AnomalyCreatedPayload();
        payload.setSeverity("CRITICAL");

        when(mockRandom.nextInt(100)).thenReturn(49);
        assertThat(defectProbabilityUtils.shouldGenerate(payload)).isTrue();

        when(mockRandom.nextInt(100)).thenReturn(50);
        assertThat(defectProbabilityUtils.shouldGenerate(payload)).isFalse();
    }

    @Test
    @DisplayName("알 수 없는 severity이면 DEFAULT_RATE(10%)가 적용된다")
    void unknownSeverity_usesDefaultRate() {
        AnomalyCreatedPayload payload = new AnomalyCreatedPayload();
        payload.setSeverity("WARNING");

        when(mockRandom.nextInt(100)).thenReturn(9);
        assertThat(defectProbabilityUtils.shouldGenerate(payload)).isTrue();

        when(mockRandom.nextInt(100)).thenReturn(10);
        assertThat(defectProbabilityUtils.shouldGenerate(payload)).isFalse();
    }
}
