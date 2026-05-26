package com.factory.management_service.ctrl;

import com.factory.common.core.dto.ApiResponse;

import org.springframework.web.bind.annotation.RestController;

import com.factory.management_service.domain.dto.AlertEquipmentResponseDTO;
import com.factory.management_service.domain.dto.AnomalyResponseDTO;
import com.factory.management_service.domain.dto.EquipmentRecipeResponseDTO;
import com.factory.management_service.domain.dto.EquipmentResponseDTO;
import com.factory.management_service.service.ManagementService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/equipments")
@RequiredArgsConstructor
public class ManagementController {

    private final ManagementService managementService;

    @GetMapping("/list/{processId}")
    public ApiResponse<List<EquipmentResponseDTO>> getEquipmentList(
            @PathVariable Long processId) {
        List<EquipmentResponseDTO> response = managementService.getEquipmentList(processId);

        return ApiResponse.ofSuccess(response);
    }

    @GetMapping("/{equipmentId}")
    public ApiResponse<EquipmentResponseDTO> getEquipmentDetail(
            @PathVariable Long equipmentId) {
        EquipmentResponseDTO response = managementService.getEquipmentDetail(equipmentId);

        return ApiResponse.ofSuccess(response);
    }

    @GetMapping("/{equipmentId}/anomaly")
    public ApiResponse<List<AnomalyResponseDTO>> getRecentAnomaly(
            @PathVariable Long equipmentId) {
        List<AnomalyResponseDTO> response = managementService.getRecentAnomaly(equipmentId);

        return ApiResponse.ofSuccess(response);
    }

    @GetMapping("/{equipmentId}/recipe")
    public ApiResponse<EquipmentRecipeResponseDTO> getCurrentRecipe(
            @PathVariable Long equipmentId) {
        EquipmentRecipeResponseDTO response = managementService.getCurrentRecipe(equipmentId);

        return ApiResponse.ofSuccess(response);
    }

    @GetMapping("/dashboard")
    public ApiResponse<List<EquipmentResponseDTO>> getDashboardList() {
        List<EquipmentResponseDTO> response = managementService.getDashboardList();
        return ApiResponse.ofSuccess(response);
    }

}