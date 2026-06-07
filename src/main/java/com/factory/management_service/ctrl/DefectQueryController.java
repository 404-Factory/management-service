package com.factory.management_service.ctrl;

import com.factory.common.core.dto.ApiResponse;
import com.factory.management_service.dao.DefectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class DefectQueryController {

    private final DefectRepository defectRepository;

    @GetMapping("/api/management/defects/count")
    public ApiResponse<Long> getDefectCount(
            @RequestParam String equipmentName,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime since
    ) {
        long count = defectRepository.countByEquipmentNameSince(equipmentName, since);
        return ApiResponse.ofSuccess(count);
    }
}
