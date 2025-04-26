package com.demo.app.domain.common.exception;

import lombok.Getter;


/**
 * The type Domain exception.
 */
@Getter
public class DomainException extends RuntimeException {

    private IErrorCode errorCode;

    /**
     * Instantiates a new Api exception.
     *
     * @param errorCode the error code
     */
    public DomainException(IErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    /**
     * Instantiates a new Api exception.
     *
     * @param errorCode the error code
     * @param cause     the cause
     */
    public DomainException(IErrorCode errorCode, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
    }

    /**
     * Instantiates a new Api exception.
     *
     * @param message the message
     * @param cause   the cause
     */
    public DomainException(String message, Throwable cause) {
        super(message, cause);
    }

}
