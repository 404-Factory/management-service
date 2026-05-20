package com.factory.management_service.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.factory.management_service.dao.AnomalyRepository;
import com.factory.management_service.dao.EquipmentRecipeRepository;
import com.factory.management_service.dao.EquipmentRepository;
import com.factory.management_service.domain.dto.AnomalyResponseDTO;
import com.factory.management_service.domain.dto.EquipmentRecipeResponseDTO;
import com.factory.management_service.domain.dto.EquipmentResponseDTO;
import com.factory.management_service.domain.entity.EquipmentEntity;
import com.factory.management_service.domain.entity.EquipmentRecipeEntity;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ManagementService {
    private final EquipmentRepository equipmentRepository;
    private final AnomalyRepository anomalyRepository;
    private final EquipmentRecipeRepository equipmentRecipeRepository;

    @Transactional(readOnly = true)
    public List<EquipmentResponseDTO> getEquipmentList(Long processId) {
        return equipmentRepository.findAll()
                .stream()
                .map(EquipmentResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EquipmentResponseDTO getEquipmentDetail(Long equipmentId) {
        EquipmentEntity response = equipmentRepository.findById(equipmentId)
                .orElseThrow(() -> new RuntimeException("설비를 찾을 수 없습니다."));
        new EquipmentResponseDTO();
        return EquipmentResponseDTO.fromEntity(response);
    }

    @Transactional(readOnly = true)
    public List<AnomalyResponseDTO> getRecentAnomaly(Long equipmentId) {
        return anomalyRepository.findAll()
                .stream()
                .map(AnomalyResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EquipmentRecipeResponseDTO getCurrentRecipe(Long equipmentId) {
        EquipmentRecipeEntity response = equipmentRecipeRepository.findByEquipment_EquipmentId(equipmentId)
                .orElseThrow(() -> new RuntimeException("레시피를 찾을 수 없습니다."));
        new EquipmentRecipeResponseDTO();
        return EquipmentRecipeResponseDTO.fromEntity(response);
    }
}
