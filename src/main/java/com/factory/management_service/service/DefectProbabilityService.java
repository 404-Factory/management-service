package com.factory.management_service.service;

import java.util.Random;

import org.springframework.stereotype.Service;

import com.factory.management_service.domain.entity.AnomalyEntity;

@Service
public class DefectProbabilityService {

    /**
     * anomaly 발생 시
     * defect 생성 여부 확률
     *
     * 현재는 테스트용 고정값 사용
     */
    private static final int DEFAULT_GENERATE_RATE = 30;

    private final Random random = new Random();

    public boolean shouldGenerate(
            AnomalyEntity anomalyLog) {

        int value = random.nextInt(100);

        return value < DEFAULT_GENERATE_RATE;
    }
}