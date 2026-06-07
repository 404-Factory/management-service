package com.factory.management.domain.rule;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DefectCandidate {
    private String defectType;
    private String defectCode;
    private int weight;
}
