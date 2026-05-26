package com.factory.management_service.scheduler;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.factory.management_service.dao.AnomalyRepository;
import com.factory.management_service.domain.entity.AnomalyEntity;
import com.factory.management_service.domain.entity.DefectEntity;
import com.factory.management_service.service.DefectCreateService;
import com.factory.management_service.service.DefectGeneratorService;
import com.factory.management_service.service.DefectProbabilityService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class DefectGenerateScheduler {

    private final AnomalyRepository anomalyRepository;

    private final DefectProbabilityService probabilityService;

    private final DefectGeneratorService generatorService;

    private final DefectCreateService createService;

    /**
     * 1분마다 최근 anomaly 조회 후
     * synthetic defect 생성
     */
    @Scheduled(fixedDelay = 60000)
    public void generate() {

        LocalDateTime from = LocalDateTime.now().minusHours(24);

        List<AnomalyEntity> anomalyLogs = anomalyRepository
                .findRecentAnomalies(from);

        for (AnomalyEntity anomalyLog : anomalyLogs) {

            try {

                boolean shouldGenerate = probabilityService
                        .shouldGenerate(anomalyLog);

                if (!shouldGenerate) {
                    continue;
                }

                DefectEntity defectInfo = generatorService
                        .generate(anomalyLog);

                if (defectInfo == null) {
                    continue;
                }

                createService.create(defectInfo);

                log.info(
                        "[DEFECT GENERATED] type={}, code={}",
                        defectInfo.getDefectType(),
                        defectInfo.getDefectCode());

            } catch (Exception e) {

                log.error(
                        "defect generate fail. anomalyId={}",
                        anomalyLog.getLogId(),
                        e);
            }
        }
    }
}