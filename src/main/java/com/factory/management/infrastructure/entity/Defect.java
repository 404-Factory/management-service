package com.factory.management.infrastructure.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "defects")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Defect {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lot_id")
    private Lot lot;

    @Column(length = 50)
    private String defectType;

    @Column(length = 50)
    private String defectCode;

    private Instant detectedTime;

    private Instant occurredTime;

    private Long causeProcessId;

    @Column(length = 100)
    private String causeProcessName;

    private Long causeEquipmentId;

    @Column(length = 100)
    private String causeEquipmentName;

    @Builder
    public Defect(Long id, Lot lot, String defectType, String defectCode, Instant detectedTime,
        Instant occurredTime, Long causeProcessId, String causeProcessName, Long causeEquipmentId,
        String causeEquipmentName) {
        this.id = id;
        this.lot = lot;
        this.defectType = defectType;
        this.defectCode = defectCode;
        this.detectedTime = detectedTime;
        this.occurredTime = occurredTime;
        this.causeProcessId = causeProcessId;
        this.causeProcessName = causeProcessName;
        this.causeEquipmentId = causeEquipmentId;
        this.causeEquipmentName = causeEquipmentName;
    }
}
