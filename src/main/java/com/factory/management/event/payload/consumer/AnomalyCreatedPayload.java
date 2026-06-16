package com.factory.management.event.payload.consumer;

import com.factory.common.event.domain.EventPayload;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.Instant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class AnomalyCreatedPayload implements EventPayload {

    private Long anomalyId;
    private Long equipmentId;
    private String equipmentName;
    private String recipeParameter;
    private String severity;
    private Instant occurredTime;
    private Instant firstDetectedAt;
    private Instant lastDetectedAt;
    private String causeRule;
}