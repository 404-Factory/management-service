package com.factory.management_service.ctrl;

import com.factory.management_service.domain.dto.EquipmentDefectCountDTO;
import com.factory.management_service.service.GrafanaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/grafana")
@RequiredArgsConstructor
public class GrafanaController {

    private final GrafanaService grafanaService;

    @GetMapping("/defect")
    public List<EquipmentDefectCountDTO> getDefectCount() {

        return grafanaService.getDefectCountByEquipment();
    }
}