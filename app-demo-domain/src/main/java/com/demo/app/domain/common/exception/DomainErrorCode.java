package com.demo.app.domain.common.exception;


/**
 * The enum Result code.
 */
public enum DomainErrorCode implements IErrorCode {
    /**
     * General failed domain error code.
     */
    GENERAL_FAILED(2000, "General business logic error"),
    /**
     * The Validation EMAID failed.
     */
    VALIDATION_EMAID_FAILED(2001, "EMAID validation failed"),
    /**
     * The Validation rfid failed.
     */
    VALIDATION_RFID_FAILED(2002, "RFID validation failed"),

    /**
     * The Validation email failed.
     */
    VALIDATION_EMAIL_FAILED(2003, "EMAIL validation failed"),
    /**
     * Insufficient business permissions
     */
    PERMISSION_DENIED(2004, "Insufficient business permissions"),
    /**
     * Illegal business state transition
     */
    ILLEGAL_STATE(2005, "Illegal business state"),
    /**
     * The Deactivated state.
     */
    DEACTIVATED_STATE(2006, "Deactivated business state");
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
