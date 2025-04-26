package com.demo.app.interfaces.common.entity;


import com.demo.app.domain.common.exception.IErrorCode;


/**
 * The enum Common result code.
 */
public enum CommonResultCode implements IErrorCode {
    /**
     * 200 OK - Standard success response
     */
    SUCCESS(200, "success"),
    /**
     * 500 Internal Server Error - General server failure
     */
    ERROR(500, "failed"),
    /**
     * 400 Request Error - Invalid request parameters or format
     */
    REQUEST_ERROR(400, "request parameter error"),
    /**
     * 401 Unauthorized - Missing or invalid authentication
     */
    UNAUTHORIZED(401, "Authentication credentials required"),
    /**
     * 403 Forbidden - Insufficient permissions
     */
    FORBIDDEN(403, "Access to the resource denied"),
    /**
     * 404 Not Found - Resource does not exist
     */
    NOT_FOUND(404, "Requested resource could not be found"),
    /**
     * 501 The Infrastructure error.
     */
    INFRASTRUCTURE_ERROR(501, "Internal access error"),
    /**
     * 502 The Domain error.
     */
    DOMAIN_ERROR(502, "Business logic error"),
    /**
     * 503 The App error.
     */
    APP_ERROR(503, "App logic error");
    private final long code;
    private final String message;

    CommonResultCode(long code, String message) {
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
