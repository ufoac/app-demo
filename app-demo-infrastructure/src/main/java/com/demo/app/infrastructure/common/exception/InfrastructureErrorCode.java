package com.demo.app.infrastructure.common.exception;


import com.demo.app.domain.common.exception.IErrorCode;


/**
 * The enum Infrastructure error code.
 */
public enum InfrastructureErrorCode implements IErrorCode {

    /**
     * The General failed.
     */
    GENERAL_FAILED(1000, "General infrastructure error"),
    OPTIMISTIC_LOCKER_FAILED(1001, "Optimistic locker error");
    private final long code;
    private final String message;

    InfrastructureErrorCode(long code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public long getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
