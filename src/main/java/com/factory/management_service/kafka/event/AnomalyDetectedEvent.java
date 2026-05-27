package com.factory.management_service.kafka.event;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnomalyDetectedEvent {

    private Long equipmentId;

    private Long equipmentRecipeId;

    private String recipeParameter;

    private String severity;

    private LocalDateTime occurredTime;

    private String causeRule;

    private String anomalyType;

    private LocalDateTime windowStartTime;

    private Integer sampleCount;

    private String detectionReason;
}