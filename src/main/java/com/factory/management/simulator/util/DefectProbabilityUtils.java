package com.factory.management.simulator.util;

<<<<<<< HEAD
import com.factory.management.event.consume.payload.SensorViolationPayload;

=======
import com.factory.management.event.payload.consumer.AnomalyCreatedPayload;
>>>>>>> origin/main
import java.util.Random;

import org.springframework.stereotype.Service;

@Service
public class DefectProbabilityUtils {

    private static final int CAUTION_RATE = 15;
    private static final int CRITICAL_RATE = 50;
    private static final int DEFAULT_RATE = 10;

    private final Random random = new Random();

    public boolean shouldGenerate(SensorViolationPayload violation) {

        int generateRate = getGenerateRate(violation);

        int value = random.nextInt(100);

        return value < generateRate;
    }

    private int getGenerateRate(SensorViolationPayload violation) {

        if (violation == null || violation.getSeverity() == null) {
            return DEFAULT_RATE;
        }

        return switch (violation.getSeverity()) {
            case "CAUTION" -> CAUTION_RATE;
            case "CRITICAL" -> CRITICAL_RATE;
            default -> DEFAULT_RATE;
        };
    }
}