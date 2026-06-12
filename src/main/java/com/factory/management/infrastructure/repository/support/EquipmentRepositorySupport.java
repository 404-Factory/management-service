package com.factory.management.infrastructure.repository.support;

import com.factory.management.dto.response.EquipmentResponse;
import java.util.List;

public interface EquipmentRepositorySupport {

    List<EquipmentResponse> fetchEquipmentsWithCondition(Long processId, String status);

    EquipmentResponse fetchEquipment(Long id);
}
