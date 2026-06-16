package com.factory.management.simulator.util;

<<<<<<< HEAD
import com.factory.management.event.consume.payload.SensorViolationPayload;
=======
import com.factory.management.event.payload.consumer.AnomalyCreatedPayload;
>>>>>>> origin/main
import com.factory.management.infrastructure.entity.Defect;
import com.factory.management.infrastructure.entity.Equipment;
import com.factory.management.infrastructure.entity.Lot;
import com.factory.management.infrastructure.repository.EquipmentRepository;
import com.factory.management.service.LotService;
import com.factory.management.simulator.registry.RuleRegistry;
import com.factory.management.simulator.rule.DefectCandidate;
import com.factory.management.simulator.type.RuleName;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DefectGenerator {

    private final LotService lotService;
    private final RuleRegistry ruleRegistry;
    private final EquipmentRepository equipmentRepository;
    private final DefectProbabilityUtils defectProbabilityUtils;

    private final Random random = new Random();

    /**
     * sensor violation 기반 synthetic defect 생성
     */
    public Defect generate(SensorViolationPayload violation) {

        if (!defectProbabilityUtils.shouldGenerate(violation)) {
            return null;
        }
        // rule candidate 조회
        List<DefectCandidate> candidates = ruleRegistry.getCandidates(
                violation.getSensorType(),
                RuleName.valueOf(violation.getRuleName()));

        // rule 없으면 생성 안함
        if (candidates.isEmpty()) {
            return null;
        }

        // weighted random defect 선택
        DefectCandidate selected = selectCandidate(candidates);

        // 랜덤 lot 선택
        Lot lot = getRandomLot(violation.getEquipmentId());

        if (lot == null) {
            return null;
        }

        return Defect.builder()
                .lot(lot)
                .defectType(selected.getDefectType())
                .defectCode(selected.getDefectCode())
                // 실제 발생 시각
                .occurredTime(violation.getDetectedAt())
                // defect 검출 시각
                .detectedTime(
                        randomDetectedTime(
                                violation.getDetectedAt()))
                // 원인 설비 snapshot
                .causeEquipmentId(violation.getEquipmentId())
                .causeEquipmentName(equipmentName(violation.getEquipmentId()))
                // 원인 공정 snapshot
                .causeProcessId(
                        processId(
                                violation.getSensorType()))
                .causeProcessName(
                        processName(
                                violation.getSensorType()))
                .build();
    }

    /**
     * defect 검출 시간 생성
     * <p>
     * sensor violation 발생 후 1 ~ 30분 랜덤
     */
    private Instant randomDetectedTime(
            Instant occurredTime) {

        int minute = random.nextInt(30) + 1;

        return occurredTime.plus(Duration.ofMinutes(minute));
    }

    /**
     * 진행 중인 lot 중 랜덤 선택
     */
    private Lot getRandomLot(Long equipmentId) {

        List<Lot> lots = lotService.getLotsByEquipmentId(equipmentId);

        if (lots.isEmpty()) {
            return null;
        }

        return lots.get(
                random.nextInt(lots.size()));
    }

    /**
     * 공정명 변환
     */
    private String processName(
            String parameter) {

        return switch (parameter) {

            case "Spin Speed",
                    "Soft Bake Temperature" ->
                "DEPOSITION";

            case "Exposure Dose",
                    "PEB" ->
                "PHOTO";

            case "Chamber Pressure",
                    "Chuck Temperature" ->
                "ETCH";

            case "Chemical Temperature",
                    "Chemical Concentration" ->
                "CLEANING";

            default -> "UNKNOWN";
        };
    }

    /**
     * 공정 ID 매핑
     * <p>
     * 테스트용 고정값
     */
    private Long processId(
            String parameter) {

        return switch (parameter) {

            case "Spin Speed",
                    "Soft Bake Temperature" ->
                1L;

            case "Exposure Dose",
                    "PEB" ->
                2L;

            case "Chamber Pressure",
                    "Chuck Temperature" ->
                3L;

            case "Chemical Temperature",
                    "Chemical Concentration" ->
                4L;

            default -> 0L;
        };
    }

    private String equipmentName(Long equipmentId) {
        return equipmentRepository.findById(equipmentId)
                .map(Equipment::getName)
                .orElse("UNKNOWN");
    }

    private DefectCandidate selectCandidate(
            List<DefectCandidate> candidates) {

        int totalWeight = candidates.stream()
                .mapToInt(DefectCandidate::getWeight)
                .sum();

        int randomValue = random.nextInt(totalWeight);

        int cumulativeWeight = 0;

        for (DefectCandidate candidate : candidates) {

            cumulativeWeight += candidate.getWeight();

            if (randomValue < cumulativeWeight) {
                return candidate;
            }
        }

        return candidates.get(0);
    }
}