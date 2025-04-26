package com.demo.app.service.common.exception;

import com.demo.app.domain.common.exception.IErrorCode;
import lombok.Getter;


/**
 * The type Domain exception.
 */
@Getter
public class AppException extends RuntimeException {

    private IErrorCode errorCode;

    /**
     * Instantiates a new Api exception.
     *
     * @param errorCode the error code
     */
    public AppException(IErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    /**
     * Instantiates a new Api exception.
     *
     * @param errorCode the error code
     * @param cause     the cause
     */
    public AppException(IErrorCode errorCode, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
    }

    /**
     * Instantiates a new Api exception.
     *
     * @param message the message
     * @param cause   the cause
     */
    public AppException(String message, Throwable cause) {
        super(message, cause);
    }

}
