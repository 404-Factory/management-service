package com.factory.management_service.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import com.factory.management_service.domain.type.AlertSeverity;
import com.factory.management_service.domain.type.AlertStatus;

@Entity
@Table(name = "ALERT")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class AlertEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alert_id")
    private Long alertId;

    @Column(name = "anomaly_id", length = 100)
    private String anomalyId;

    @Column(name = "equipment_id", length = 100)
    private String equipmentId;

    @Column(name = "title", length = 200, nullable = false)
    private String title;

    @Column(name = "message", length = 500, nullable = false)
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 30, nullable = false)
    private AlertStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "severity", length = 30, nullable = false)
    private AlertSeverity severity;

    @Column(name = "created_at", updatable = false, nullable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @LastModifiedDate
    private LocalDateTime updatedAt;

}
