package com.factory.management_service.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.factory.management_service.dao.LotRepository;
import com.factory.management_service.dao.EquipmentRepository;
import com.factory.common.event.payload.AnomalyDetectedEventPayload;
import com.factory.management_service.domain.entity.DefectEntity;
import com.factory.management_service.domain.entity.LotEntity;
import com.factory.management_service.domain.entity.EquipmentEntity;
import com.factory.management_service.domain.rule.DefectCandidate;
import com.factory.management_service.domain.type.RuleName;
import com.factory.management_service.registry.RuleRegistry;

import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
@RequiredArgsConstructor
@Transactional
public class DefectGeneratorService {

    private final RuleRegistry ruleRegistry;
    private final LotRepository lotRepository;
    private final EquipmentRepository equipmentRepository;

    private final Random random = new Random();

    /**
     * anomaly 기반 synthetic defect 생성
     */
    public DefectEntity generate(AnomalyDetectedEventPayload anomaly) {

        // rule candidate 조회
        List<DefectCandidate> candidates = ruleRegistry.getCandidates(
                anomaly.getRecipeParameter(),
                RuleName.valueOf(anomaly.getCauseRule()));

        // rule 없으면 생성 안함
        if (candidates.isEmpty()) {
            return null;
        }

        // weighted random defect 선택
        DefectCandidate selected = selectCandidate(candidates);

        // 랜덤 lot 선택
        LotEntity lot = getRandomLot();

        if (lot == null) {
            return null;
        }

        EquipmentEntity equipment = equipmentRepository.findById(anomaly.getEquipmentId())
                .orElse(null);

        LocalDateTime occurredTime = LocalDateTime.ofInstant(anomaly.getOccurredTime(), ZoneOffset.UTC);

        return DefectEntity.builder()

                .lot(lot)

                .defectType(
                        selected.getDefectType())

                .defectCode(
                        selected.getDefectCode())

                // 실제 발생 시각
                .occurredTime(occurredTime)

                // defect 검출 시각
                .detectedTime(
                        randomDetectedTime(occurredTime))

                // 원인 설비 snapshot
                .causeEquipmentId(
                        anomaly.getEquipmentId())

                .causeEquipmentName(
                        equipment != null ? equipment.getEquipmentName() : "UNKNOWN")

                // 원인 공정 snapshot
                .causeProcessId(
                        processId(
                                anomaly.getRecipeParameter()))

                .causeProcessName(
                        processName(
                                anomaly.getRecipeParameter()))

                .build();
    }

    /**
     * defect 검출 시간 생성
     *
     * anomaly 발생 후
     * 1 ~ 30분 랜덤
     */
    private LocalDateTime randomDetectedTime(
            LocalDateTime occurredTime) {

        int minute = random.nextInt(30) + 1;

        return occurredTime.plusMinutes(minute);
    }

    /**
     * 진행 중인 lot 중 랜덤 선택
     */
    private LotEntity getRandomLot() {

        List<LotEntity> lots = lotRepository.findAll();

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
     *
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