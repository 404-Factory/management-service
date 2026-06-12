package com.factory.management.service;

import com.factory.management.infrastructure.entity.Lot;
import com.factory.management.infrastructure.repository.LotRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LotServiceImpl implements LotService {

    private final LotRepository lotRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Lot> getAllLots() {
        return lotRepository.findAll();
    }
}
