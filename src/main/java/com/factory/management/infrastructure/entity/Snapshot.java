package com.factory.management.infrastructure.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "snapshots")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Snapshot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long anomalyId;

    private String url;

    @Builder
    public Snapshot(Long id, Long anomalyId, String url) {
        this.id = id;
        this.anomalyId = anomalyId;
        this.url = url;
    }
}
