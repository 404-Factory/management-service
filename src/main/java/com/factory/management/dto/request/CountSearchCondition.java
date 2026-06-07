package com.factory.management.dto.request;

import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class CountSearchCondition {

    private String equipmentName;
    private LocalDate startDate;
    private LocalDate endDate;
}
