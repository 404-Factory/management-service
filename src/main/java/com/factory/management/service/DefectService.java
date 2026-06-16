package com.factory.management.service;

import com.factory.management.event.consume.payload.SensorViolationPayload;

import java.time.LocalDate;

public interface DefectService {

    long getCount(String equipmentName, LocalDate startDate, LocalDate endDate);

    void createWithProbability(SensorViolationPayload payload);
}
