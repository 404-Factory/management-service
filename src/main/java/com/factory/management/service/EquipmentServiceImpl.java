package com.factory.management.service;

import com.factory.management.dto.response.EquipmentRecipeResponse;
import com.factory.management.dto.response.EquipmentResponse;
import com.factory.management.exception.ManagementErrorCode;
import com.factory.management.exception.ManagementException;
import com.factory.management.infrastructure.repository.EquipmentRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EquipmentServiceImpl implements EquipmentService {

    private final EquipmentRepository equipmentRepository;

    @Override
    @Transactional(readOnly = true)
    public List<EquipmentResponse> getAllEquipments(Long processId, String status) {

        return equipmentRepository.fetchEquipmentsWithCondition(processId, status);
    }

    @Override
    @Transactional(readOnly = true)
    public EquipmentResponse getEquipment(Long id) {

        EquipmentResponse response = equipmentRepository.fetchEquipment(id);
        if (response == null) {
            throw new ManagementException(ManagementErrorCode.EQUIPMENT_NOT_FOUND);
        }
        return response;
    }
}
