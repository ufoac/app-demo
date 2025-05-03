package com.demo.app.interfaces.common.entity;


import com.demo.app.domain.common.exception.IErrorCode;
import lombok.Data;

/**
 * The type Common result.
 *
 * @param <T> the type parameter
 */
@Data
public class CommonResult<T> {
    private boolean ok = true;
    private T data;
    private long code;
    private String message;


    /**
     * Instantiates a new Common result.
     */
    protected CommonResult() {
    }

    /**
     * Instantiates a new Common result.
     *
     * @param code    the code
     * @param message the message
     * @param data    the data
     */
    protected CommonResult(long code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * Instantiates a new Common result.
     *
     * @param code    the code
     * @param message the message
     * @param ok      the ok
     * @param data    the data
     */
    protected CommonResult(long code, String message, boolean ok, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.ok = ok;
    }

    /**
     * Success common result.
     *
     * @param <T>  the type parameter
     * @param data 获取的数据
     * @return the common result
     */
    public static <T> CommonResult<T> success(T data) {
        return new CommonResult<>(CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage(), true, data);
    }

    /**
     * Success common result.
     *
     * @param <T>     the type parameter
     * @param data    获取的数据
     * @param message 提示信息
     * @return the common result
     */
    public static <T> CommonResult<T> success(T data, String message) {
        return new CommonResult<>(CommonResultCode.SUCCESS.getCode(), message, data);
    }

    /**
     * Failed common result.
     *
     * @param <T>       the type parameter
     * @param errorCode 错误码
     * @return the common result
     */
    public static <T> CommonResult<T> failed(IErrorCode errorCode) {
        return new CommonResult<>(errorCode.getCode(), errorCode.getMessage(), false, null);
    }

    /**
     * Failed common result.
     *
     * @param <T>       the type parameter
     * @param errorCode 错误码
     * @param message   错误信息
     * @return the common result
     */
    public static <T> CommonResult<T> failed(IErrorCode errorCode, String message) {
        return new CommonResult<>(errorCode.getCode(), message, false, null);
    }

    /**
     * Failed common result.
     *
     * @param <T>     the type parameter
     * @param message 提示信息
     * @return the common result
     */
    public static <T> CommonResult<T> failed(String message) {
        return new CommonResult<>(CommonResultCode.ERROR.getCode(), message, false, null);
    }

    /**
     * Failed common result.
     *
     * @param <T> the type parameter
     * @return the common result
     */
    public static <T> CommonResult<T> failed() {
        return failed(CommonResultCode.ERROR);
    }

    /**
     * Request error common result.
     *
     * @param <T> the type parameter
     * @return the common result
     */
    public static <T> CommonResult<T> requestError() {
        return failed(CommonResultCode.REQUEST_ERROR);
    }

    /**
     * Domain error common result.
     *
     * @param <T> the type parameter
     * @return the common result
     */
    public static <T> CommonResult<T> domainError() {
        return failed(CommonResultCode.DOMAIN_ERROR);
    }

    /**
     * Request error common result.
     *
     * @param <T>     the type parameter
     * @param message the message
     * @return the common result
     */
    public static <T> CommonResult<T> requestError(String message) {
        return failed(CommonResultCode.REQUEST_ERROR, message);
    }

    /**
     * Domain error common result.
     *
     * @param <T>     the type parameter
     * @param message the message
     * @return the common result
     */
    public static <T> CommonResult<T> domainError(String message) {
        return failed(CommonResultCode.DOMAIN_ERROR, message);
    }

    public static <T> CommonResult<T> underlayError(String message) {
        return failed(CommonResultCode.INFRASTRUCTURE_ERROR, message);
    }
    public static <T> CommonResult<T> appError(String message) {
        return failed(CommonResultCode.APP_ERROR, message);
    }
    /**
     * Unauthorized common result.
     *
     * @param <T> the type parameter
     * @return the common result
     */
    public static <T> CommonResult<T> unauthorized() {
        return failed(CommonResultCode.UNAUTHORIZED);
    }

    /**
     * Forbidden common result.
     *
     * @param <T> the type parameter
     * @return the common result
     */
    public static <T> CommonResult<T> forbidden() {
        return failed(CommonResultCode.FORBIDDEN);
    }
}
