package com.factory.management_service.service;

import com.factory.management_service.dao.DefectRepository;
import com.factory.management_service.domain.dto.EquipmentDefectCountDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GrafanaService {

    private final DefectRepository defectRepository;

    public List<EquipmentDefectCountDTO> getDefectCountByEquipment() {

        return defectRepository.countDefectsByEquipment()
                .stream()
                .map(row -> new EquipmentDefectCountDTO(
                        (Long) row[0],
                        (Long) row[1]))
                .toList();
    }
}
