package com.demo.app.domain.common.exception;


/**
 * The enum Result code.
 */
public enum DomainErrorCode implements IErrorCode {
    /**
     * 2000, General failed domain error code.
     */
    GENERAL_FAILED(2000, "General business logic error"),

    /**
     * 2001 - The Validation EMAID failed.
     */
    VALIDATION_EMAID_FAILED(2001, "EMAID validation failed"),

    /**
     * 2002 - Insufficient business permissions
     */
    PERMISSION_DENIED(2002, "Insufficient business permissions"),
    /**
     * 2003 - Illegal business state transition
     */
    ILLEGAL_STATE(2003, "Illegal business state");

    private final long code;
    private final String message;

    DomainErrorCode(long code, String message) {
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
