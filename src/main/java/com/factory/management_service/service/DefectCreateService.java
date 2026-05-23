package com.factory.management_service.service;

import org.springframework.stereotype.Service;

import com.factory.management_service.dao.DefectRepository;
import com.factory.management_service.domain.entity.DefectEntity;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DefectCreateService {

    private final DefectRepository defectRepository;

    public void create(
            DefectEntity defectEntity) {

        defectRepository.save(defectEntity);
    }
}
