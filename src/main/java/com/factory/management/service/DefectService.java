package com.factory.management.service;

import com.factory.management.dto.response.DefectCountResponse;
import java.time.LocalDate;

public interface DefectService {

    DefectCountResponse getCount(String equipmentName, LocalDate startDate, LocalDate endDate);
}
