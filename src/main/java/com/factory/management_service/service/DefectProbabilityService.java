package com.factory.management_service.service;

import java.util.Random;

import org.springframework.stereotype.Service;

import com.factory.common.event.payload.AnomalyDetectedEventPayload;

@Service
public class DefectProbabilityService {

    private static final int CAUTION_RATE = 15;
    private static final int CRITICAL_RATE = 50;
    private static final int DEFAULT_RATE = 10;

    private final Random random = new Random();

    public boolean shouldGenerate(AnomalyDetectedEventPayload anomalyLog) {

        int generateRate = getGenerateRate(anomalyLog);

        int value = random.nextInt(100);

        return value < generateRate;
    }

    private int getGenerateRate(AnomalyDetectedEventPayload anomalyLog) {

        if (anomalyLog == null || anomalyLog.getSeverity() == null) {
            return DEFAULT_RATE;
        }

        String severity = anomalyLog.getSeverity().toUpperCase().trim();
        return switch (severity) {
            case "CAUTION" -> CAUTION_RATE;
            case "CRITICAL" -> CRITICAL_RATE;
            default -> DEFAULT_RATE;
        };
    }
}