package com.factory.management.event.consume.payload;

import com.factory.common.event.domain.EventPayload;
import java.time.Instant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class AnomalyCreatedPayload implements EventPayload {

    private Long equipmentId;
    private String equipmentName;
    private String recipeParameter;
    private String severity;
    private Instant occurredTime;
    private String causeRule;
}