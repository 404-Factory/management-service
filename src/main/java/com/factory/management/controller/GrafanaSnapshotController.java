package com.factory.management.controller;

import java.time.LocalDate;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.factory.management.dto.response.SnapshotResponse;
import com.factory.management.service.GrafanaSnapshotService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/logs")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class GrafanaSnapshotController {

    private final GrafanaSnapshotService grafanaSnapshotService;

    @GetMapping("/snapshot")
    public ResponseEntity<SnapshotResponse> getLogSnapshot(@RequestParam("uid") String dashboardUid,
            @RequestParam("from") String from,
            @RequestParam("to") String to) {

        String externalSnapshotUrl = grafanaSnapshotService.createSnapshot(dashboardUid, from, to);

        SnapshotResponse response = SnapshotResponse.builder()
                .url(externalSnapshotUrl)
                .key(dashboardUid)
                .deleteUrl("")
                .build();

        return ResponseEntity.ok(response);
    }
}
