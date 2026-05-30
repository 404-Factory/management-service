package com.factory.management_service.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "DEFECT_INFO")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DefectEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long defectId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lot_id")
    private LotEntity lot;

    @Column(length = 50)
    private String defectType;

    @Column(length = 50)
    private String defectCode;

    private LocalDateTime detectedTime;

    private LocalDateTime occurredTime;

    private Long causeProcessId;

    @Column(length = 100)
    private String causeProcessName;

    private Long causeEquipmentId;

    @Column(length = 100)
    private String causeEquipmentName;
}