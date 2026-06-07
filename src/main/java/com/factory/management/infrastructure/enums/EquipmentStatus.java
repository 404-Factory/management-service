package com.factory.management.infrastructure.enums;

import com.factory.management.exception.ManagementErrorCode;
import com.factory.management.exception.ManagementException;

public enum EquipmentStatus {
    NORMAL("normal"),
    WARNING("warning"),
    CRITICAL("critical");

    private final String code;

    EquipmentStatus(String code) {
        this.code = code;
    }

    public static EquipmentStatus fromCode(String code) {
        try {
            return EquipmentStatus.valueOf(code.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ManagementException(ManagementErrorCode.INVALID_EQUIPMENT_STATUS);
        }
    }
}
