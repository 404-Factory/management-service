package com.factory.management_service.ctrl;

import com.factory.common.core.dto.ApiResponse;

import org.springframework.web.bind.annotation.RestController;

import com.factory.management_service.domain.dto.EquipmentRecipeResponseDTO;
import com.factory.management_service.domain.dto.EquipmentResponseDTO;
import com.factory.management_service.service.ManagementService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequiredArgsConstructor
public class ManagementController {

    private final ManagementService managementService;

    @GetMapping("/api/management/equipments/list/{processId}")
    public ApiResponse<List<EquipmentResponseDTO>> getEquipmentList(
            @PathVariable Long processId) {
        List<EquipmentResponseDTO> response = managementService.getEquipmentList(processId);

        return ApiResponse.ofSuccess(response);
    }

    @GetMapping("/api/management/equipments/{equipmentId}")
    public ApiResponse<EquipmentResponseDTO> getEquipmentDetail(
            @PathVariable Long equipmentId) {
        EquipmentResponseDTO response = managementService.getEquipmentDetail(equipmentId);

        return ApiResponse.ofSuccess(response);
    }

    @GetMapping("/api/management/recipes/{equipmentId}")
    public ApiResponse<EquipmentRecipeResponseDTO> getCurrentRecipe(
            @PathVariable Long equipmentId) {
        EquipmentRecipeResponseDTO response = managementService.getCurrentRecipe(equipmentId);

        return ApiResponse.ofSuccess(response);
    }

    @GetMapping("/api/management/equipments/dashboard")
    public ApiResponse<List<EquipmentResponseDTO>> getDashboardList() {
        List<EquipmentResponseDTO> response = managementService.getDashboardList();
        return ApiResponse.ofSuccess(response);
    }

}