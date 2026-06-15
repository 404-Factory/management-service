package com.factory.management.service;

import com.factory.management.event.payload.consumer.AnomalyCreatedPayload;
import com.factory.management.infrastructure.entity.Defect;
import com.factory.management.infrastructure.repository.DefectRepository;
import com.factory.management.simulator.util.DefectGenerator;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefectServiceTest {

    @Mock
    private DefectRepository defectRepository;

    @Mock
    private DefectGenerator defectGenerator;

    @InjectMocks
    private DefectServiceImpl defectService;

    @Test
    @DisplayName("설비 이름과 기간 조건으로 불량 개수를 올바르게 요청하고 전달받는지 검증한다")
    void getCountTest() {
        // given
        String equipmentName = "EQ-01";
        LocalDate startDate = LocalDate.of(2026, 6, 8);
        LocalDate endDate = LocalDate.of(2026, 6, 8);
        when(defectRepository.getDefectCount(equipmentName, startDate, endDate)).thenReturn(5L);

        // when
        long count = defectService.getCount(equipmentName, startDate, endDate);

        // then
        assertThat(count).isEqualTo(5L);
        verify(defectRepository).getDefectCount(equipmentName, startDate, endDate);
    }

    @Test
    @DisplayName("확률적으로 불량 대상이 감지되면 불량을 생성하고 저장한다")
    void createWithProbabilitySuccessTest() {
        // given
        AnomalyCreatedPayload payload = new AnomalyCreatedPayload();
        Defect defect = Defect.builder().defectCode("DF-01").build();
        when(defectGenerator.generate(payload)).thenReturn(defect);

        // when
        defectService.createWithProbability(payload);

        // then
        verify(defectGenerator).generate(payload);
        verify(defectRepository).save(defect);
    }

    @Test
    @DisplayName("확률적으로 불량 대상이 감지되지 않으면 저장하지 않는다")
    void createWithProbabilitySkipTest() {
        // given
        AnomalyCreatedPayload payload = new AnomalyCreatedPayload();
        when(defectGenerator.generate(payload)).thenReturn(null);

        // when
        defectService.createWithProbability(payload);

        // then
        verify(defectGenerator).generate(payload);
        verify(defectRepository, never()).save(any(Defect.class));
    }
}
