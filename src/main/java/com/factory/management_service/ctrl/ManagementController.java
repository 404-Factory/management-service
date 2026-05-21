package com.factory.management_service.ctrl;

import org.springframework.web.bind.annotation.RestController;

import com.factory.management_service.domain.dto.AnomalyResponseDTO;
import com.factory.management_service.domain.dto.EquipmentRecipeResponseDTO;
import com.factory.management_service.domain.dto.EquipmentResponseDTO;
import com.factory.management_service.service.ManagementService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/equipments")
@RequiredArgsConstructor
public class ManagementController {
    private final ManagementService managementService;

    @GetMapping("/list/{processId}")
    public ResponseEntity<List<EquipmentResponseDTO>> getEquipmentList(@PathVariable Long processId) {
        List<EquipmentResponseDTO> response = managementService.getEquipmentList(processId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{equipmentId}")
    public ResponseEntity<EquipmentResponseDTO> getEquipmentDetail(@PathVariable Long equipmentId) {
        EquipmentResponseDTO response = managementService.getEquipmentDetail(equipmentId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{equipmentId}/anomaly")
    public ResponseEntity<List<AnomalyResponseDTO>> getRecentAnomaly(@PathVariable Long equipmentId) {
        List<AnomalyResponseDTO> response = managementService.getRecentAnomaly(equipmentId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{equipmentId}/recipe")
    public ResponseEntity<EquipmentRecipeResponseDTO> getCurrentRecipe(@PathVariable Long equipmentId) {
        EquipmentRecipeResponseDTO response = managementService.getCurrentRecipe(equipmentId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}