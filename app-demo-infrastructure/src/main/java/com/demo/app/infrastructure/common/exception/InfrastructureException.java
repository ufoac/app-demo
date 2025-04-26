package com.demo.app.infrastructure.common.exception;

import com.demo.app.domain.common.exception.IErrorCode;
import lombok.Getter;


/**
 * The type Domain exception.
 */
@Getter
public class InfrastructureException extends RuntimeException {

    private IErrorCode errorCode;

    /**
     * Instantiates a new Api exception.
     *
     * @param errorCode the error code
     */
    public InfrastructureException(IErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    /**
     * Instantiates a new Api exception.
     *
     * @param errorCode the error code
     * @param cause     the cause
     */
    public InfrastructureException(IErrorCode errorCode, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
    }

    /**
     * Instantiates a new Api exception.
     *
     * @param message the message
     * @param cause   the cause
     */
    public InfrastructureException(String message, Throwable cause) {
        super(message, cause);
    }

}
