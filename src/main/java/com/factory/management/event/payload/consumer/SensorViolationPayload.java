package com.factory.management.event.consume.payload;

import java.time.Instant;

import com.factory.common.event.domain.EventPayload;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class SensorViolationPayload implements EventPayload {
    private Long equipmentId;
    private String sensorId;
    private String sensorType;
    private String ruleName;
    private String anomalyType;
    private String severity;
    private Double measuredValue;
    private Double referenceValue;
    private Double deviation;
    private Double deviationRate;
    private Double min;
    private Double max;
    private Instant detectedAt;
    private Instant windowStart;
    private Instant windowEnd;
    private Integer sampleCount;
    private String reason;
}
