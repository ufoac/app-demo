package com.demo.app.service.common.exception;


import com.demo.app.domain.common.exception.IErrorCode;


/**
 * The enum Infrastructure error code.
 */
public enum AppErrorCode implements IErrorCode {
    /**
     * The General failed.
     */
    GENERAL_FAILED(3000, "General Application error"),
    NOT_FOUND(3001, "Target not found"),
    EMAIL_DUPLICATED(3002, "Email duplicated");
    private final long code;
    private final String message;

    AppErrorCode(long code, String message) {
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
