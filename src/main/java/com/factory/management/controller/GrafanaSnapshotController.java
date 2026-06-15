package com.factory.management.controller;

import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<String> getSnapshot(
            @PathVariable Long anomalyId) {
        return ResponseEntity.ok(snapshotService.getSnapshotUrl(anomalyId));
    }
}
