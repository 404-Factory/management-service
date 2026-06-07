package com.factory.management.service;

import java.time.LocalDate;

public interface DefectService {

    long getCount(String equipmentName, LocalDate startDate, LocalDate endDate);
}
