package com.factory.management.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class EquipmentResponse {

    private Long id;
    private String name;
    private Long processId;
    private String processName;
    private String status;

    @Builder
    public EquipmentResponse(Long id, String name, Long processId, String processName,
        String status) {
        this.id = id;
        this.name = name;
        this.processId = processId;
        this.processName = processName;
        this.status = status;
    }

}
