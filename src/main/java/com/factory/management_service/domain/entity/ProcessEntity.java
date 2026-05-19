package com.factory.management_service.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "PROCESS_INFO")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ProcessEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "process_id")
    private Long processId;

    @Column(name = "process_name", length = 100)
    private String processName;
}