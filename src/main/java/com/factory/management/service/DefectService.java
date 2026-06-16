package com.factory.management.service;

import com.factory.management.event.payload.consumer.SensorViolationPayload;

import java.time.LocalDateTime;

public interface DefectService {

    long getCount(String equipmentName, LocalDateTime since);

    void createWithProbability(SensorViolationPayload payload);
}
