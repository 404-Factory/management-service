package com.factory.management.controller;

import com.factory.management.dto.request.CountSearchCondition;
import com.factory.management.service.DefectService;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/management/defects")
@RequiredArgsConstructor
public class DefectController {

    private final DefectService defectService;

    @GetMapping("/count")
    public ResponseEntity<Long> getCount(
        @ModelAttribute CountSearchCondition condition) {
        String equipmentName = condition.getEquipmentName();
        LocalDateTime since = condition.getSince();

        log.info("getDefectCount equipmentName: {}, since: {}", equipmentName, since);

        return ResponseEntity.ok(defectService.getCount(equipmentName, since));
    }
}
