package com.factory.management_service.ctrl;

import org.springframework.web.bind.annotation.RestController;

import com.factory.management_service.domain.dto.AnomalyResponseDTO;
import com.factory.management_service.domain.dto.EquipmentRecipeResponseDTO;
import com.factory.management_service.domain.dto.EquipmentResponseDTO;
import com.factory.management_service.service.ManagementService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/equipments")
@RequiredArgsConstructor
public class ManagementController {
    private final ManagementService managementService;

    @GetMapping("/{processId}/list")
    public ResponseEntity<List<EquipmentResponseDTO>> getEquipmentList(@RequestParam Long processId) {
        List<EquipmentResponseDTO> response = managementService.getEquipmentList(processId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    
    @GetMapping("/{equipmentId}")
    public ResponseEntity<EquipmentResponseDTO> getEquipmentDetail(@RequestParam Long equipmentId) {
        EquipmentResponseDTO response = managementService.getEquipmentDetail(equipmentId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{equipmentId}/anomaly")
    public ResponseEntity<List<AnomalyResponseDTO>> getRecentAnomaly(@RequestParam Long equipmentId) {
        List<AnomalyResponseDTO> response = managementService.getRecentAnomaly(equipmentId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{equipmentId}/recipe")
    public ResponseEntity<EquipmentRecipeResponseDTO> getCurrentRecipe(@RequestParam Long equipmentId) {
        EquipmentRecipeResponseDTO response = managementService.getCurrentRecipe(equipmentId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}