package com.demo.app.domain.common.exception;


/**
 * The interface Error code.
 */
public interface IErrorCode {
    /**
     * 返回码
     *
     * @return the code
     */
    long getCode();

    /**
     * 返回信息
     *
     * @return the message
     */
    String getMessage();
}
