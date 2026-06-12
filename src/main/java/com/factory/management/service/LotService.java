package com.factory.management.service;

import com.factory.management.infrastructure.entity.Lot;
import java.util.List;

public interface LotService {

    List<Lot> getAllLots();

    List<Lot> getLotsByEquipmentId(Long equipmentId);
}
