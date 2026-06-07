package com.factory.management.exception;

import com.factory.common.core.exception.BaseException;
import com.factory.common.core.exception.ErrorCode;

public class ManagementException extends BaseException {

    public ManagementException(ErrorCode errorCode) {
        super(errorCode);
    }

    public ManagementException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}
