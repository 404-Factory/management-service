package com.factory.management.service;

import com.factory.management.dto.response.EquipmentResponse;
import java.util.List;

public interface EquipmentService {

    List<EquipmentResponse> getAllEquipments(Long processId, String status);

    EquipmentResponse getEquipment(Long id);
}
