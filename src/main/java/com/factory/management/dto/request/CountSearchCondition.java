package com.factory.management.dto.request;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class CountSearchCondition {

    private String equipmentName;

    // 이 시각 이후의 defect를 카운트 (analysis-service가 ISO LocalDateTime으로 전달: 예) 2026-05-17T00:00)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime since;
}
