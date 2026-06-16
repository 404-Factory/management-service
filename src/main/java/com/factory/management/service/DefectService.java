package com.factory.management.service;

<<<<<<< HEAD
import com.factory.management.event.consume.payload.SensorViolationPayload;

=======
import com.factory.management.event.payload.consumer.AnomalyCreatedPayload;
>>>>>>> origin/main
import java.time.LocalDate;

public interface DefectService {

    long getCount(String equipmentName, LocalDate startDate, LocalDate endDate);

    void createWithProbability(SensorViolationPayload payload);
}
