package com.factory.management.simulator.rule;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DefectCandidate {
    private String defectType;
    private String defectCode;
    private int weight;
}
