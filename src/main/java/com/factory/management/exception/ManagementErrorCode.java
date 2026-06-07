package com.factory.management.exception;

import com.factory.common.core.exception.ErrorCode;

public enum ManagementErrorCode implements ErrorCode {
    INVALID_EQUIPMENT_STATUS(400, "MN001", "Invalid status value."),
    INVALID_PROCESS_ID(400, "MN002", "Invalid process ID."),
    EQUIPMENT_NOT_FOUND(404, "MN003", "Equipment not found."),
    EQUIPMENT_RECIPE_NOT_FOUND(404, "MN004", "Equipment recipe not found.");

    private final int status;
    private final String code;
    private final String message;

    ManagementErrorCode(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    @Override
    public int getStatus() {
        return this.status;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
