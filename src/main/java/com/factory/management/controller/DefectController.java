package com.factory.management.controller;

import com.factory.management.dto.request.CountSearchCondition;
import com.factory.management.service.DefectService;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/managemnt/defects")
@RequiredArgsConstructor
public class DefectController {

    private final DefectService defectService;

    @GetMapping("/count")
    public ResponseEntity<Long> getCount(
        @ModelAttribute CountSearchCondition condition) {
        String equipmentName = condition.getEquipmentName();
        LocalDate startDate = condition.getStartDate();
        LocalDate endDate = condition.getEndDate();

        log.info("getEquipments");
        log.info("equipmentName: {}, startDate: {}, endDate: {}", equipmentName, startDate,
            endDate);

        return ResponseEntity.ok(defectService.getCount(equipmentName, startDate, endDate));
    }
}
