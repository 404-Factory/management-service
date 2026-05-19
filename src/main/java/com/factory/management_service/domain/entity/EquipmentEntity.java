package com.factory.management_service.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "EQUIPMENT_INFO")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class EquipmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "equipment_id")
    private Long equipmentId;

    @Column(name = "equipment_name", length = 100)
    private String equipmentName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "process_id")
    private ProcessEntity process;
}