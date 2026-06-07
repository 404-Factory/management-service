package com.factory.management_service.kafka.event;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DefectCreatedEvent {

    @Builder.Default
    private String eventType = "DefectCreated";

    private Long defectId;

    private Long lotId;

    private String defectType;

    private String defectCode;

    private LocalDateTime detectedTime;

    private LocalDateTime occurredTime;

    private Long causeProcessId;

    private String causeProcessName;

    private Long causeEquipmentId;

    private String causeEquipmentName;
}