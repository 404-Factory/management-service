package com.factory.management.simulator.service;

import java.util.Random;

import org.springframework.stereotype.Service;

@Service
public class DefectProbabilityService {

    private static final int CAUTION_RATE = 15;
    private static final int CRITICAL_RATE = 50;
    private static final int DEFAULT_RATE = 10;

    private final Random random = new Random();

    public boolean shouldGenerate(AnomalyEntity anomalyLog) {

        int generateRate = getGenerateRate(anomalyLog);

        int value = random.nextInt(100);

        return value < generateRate;
    }

    private int getGenerateRate(AnomalyEntity anomalyLog) {

        if (anomalyLog == null || anomalyLog.getSeverity() == null) {
            return DEFAULT_RATE;
        }

        switch (anomalyLog.getSeverity()) {
            case CAUTION:
                return CAUTION_RATE;

            case CRITICAL:
                return CRITICAL_RATE;

            default:
                return DEFAULT_RATE;
        }
    }
}