package com.factory.management.dto.request;

import com.factory.management.validation.constraints.ValidStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class EquipmentSearchCondition {

    private Long processId;
    @ValidStatus
    private String status;
}
