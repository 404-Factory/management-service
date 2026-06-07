package com.factory.management.simulator.service;

import org.springframework.stereotype.Service;

import com.factory.management.infrastructure.repository.DefectRepository;
import com.factory.management.domain.entity.DefectEntity;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DefectCreateService {

    private final DefectRepository defectRepository;

    public void create(DefectEntity defectEntity) {

        DefectEntity saved = defectRepository.save(defectEntity);
    }
}
