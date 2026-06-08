package com.factory.management.infrastructure.repository;

import com.factory.management.infrastructure.entity.Defect;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class DefectRepositoryTest {

    @Autowired
    private DefectRepository defectRepository;

    @Test
    @DisplayName("설비 이름과 기간 조건으로 불량 개수를 조회한다")
    void getDefectCountTest() {
        // given
        Instant occurredTime = LocalDate.of(2026, 6, 8)
            .atStartOfDay(ZoneOffset.UTC)
            .toInstant();

        Defect defect1 = Defect.builder()
            .defectType("PARTICLE")
            .defectCode("DF-01")
            .causeEquipmentName("EQ-01")
            .occurredTime(occurredTime)
            .build();

        Defect defect2 = Defect.builder()
            .defectType("SCRATCH")
            .defectCode("DF-02")
            .causeEquipmentName("EQ-01")
            .occurredTime(occurredTime.plusSeconds(3600)) // 1 hour later
            .build();

        Defect defect3 = Defect.builder()
            .defectType("PARTICLE")
            .defectCode("DF-01")
            .causeEquipmentName("EQ-02")
            .occurredTime(occurredTime)
            .build();

        defectRepository.save(defect1);
        defectRepository.save(defect2);
        defectRepository.save(defect3);

        // when & then
        long count1 = defectRepository.getDefectCount("EQ-01", LocalDate.of(2026, 6, 8), LocalDate.of(2026, 6, 8));
        assertThat(count1).isEqualTo(2);

        long count2 = defectRepository.getDefectCount("EQ-02", LocalDate.of(2026, 6, 8), LocalDate.of(2026, 6, 8));
        assertThat(count2).isEqualTo(1);

        long count3 = defectRepository.getDefectCount("EQ-01", LocalDate.of(2026, 6, 9), LocalDate.of(2026, 6, 9));
        assertThat(count3).isEqualTo(0);
    }
}
