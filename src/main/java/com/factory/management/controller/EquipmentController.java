package com.factory.management.controller;

import com.factory.management.dto.request.EquipmentSearchCondition;
import com.factory.management.dto.response.EquipmentRecipeResponse;
import com.factory.management.dto.response.EquipmentResponse;
import com.factory.management.service.EquipmentRecipeService;
import com.factory.management.service.EquipmentService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/management/equipments")
@RequiredArgsConstructor
public class EquipmentController {

    private final EquipmentService equipmentService;
    private final EquipmentRecipeService equipmentRecipeService;

    @GetMapping
    public ResponseEntity<List<EquipmentResponse>> getEquipments(
        @Valid @ModelAttribute EquipmentSearchCondition condition) {
        Long processId = condition.getProcessId();
        String status = condition.getStatus();
        log.info("getEquipments");
        log.info("processId: {}, status: {}", processId, status);
        return ResponseEntity.ok(equipmentService.getAllEquipments(processId, status));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EquipmentResponse> getEquipment(
        @PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(equipmentService.getEquipment(id));
    }

    @GetMapping("/{id}/recipe")
    public ResponseEntity<EquipmentRecipeResponse> getEquipmentRecipe(
        @PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(equipmentRecipeService.getEquipmentRecipeByEquipmentId(id));
    }
}
