package com.factory.management.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.factory.management.service.SnapshotService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/snapshots")
public class GrafanaSnapshotController {

    private final SnapshotService snapshotService;

    @GetMapping("/{anomalyId}")
    public String getSnapshot(
            @PathVariable Long anomalyId) {
        return snapshotService
                .getSnapshotUrl(anomalyId);
    }
}
