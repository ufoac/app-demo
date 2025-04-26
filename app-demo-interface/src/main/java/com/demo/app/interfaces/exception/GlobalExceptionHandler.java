package com.demo.app.interfaces.exception;

import com.demo.app.domain.common.exception.DomainException;
import com.demo.app.infrastructure.common.exception.InfrastructureException;
import com.demo.app.interfaces.common.entity.CommonResult;
import com.demo.app.service.common.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * The type Global exception handler.
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Handle common result.
     *
     * @param e the e
     * @return the common result
     */
    @ResponseBody
    @ExceptionHandler(value = DomainException.class)
    public CommonResult<?> handle(DomainException e) {
        if (e.getErrorCode() == null) {
            return CommonResult.domainError(e.getMessage());
        }
        return CommonResult.failed(e.getErrorCode());
    }

    /**
     * Handle common result.
     *
     * @param e the e
     * @return the common result
     */
    @ResponseBody
    @ExceptionHandler(value = InfrastructureException.class)
    public CommonResult<?> handle(InfrastructureException e) {
        if (e.getErrorCode() == null) {
            return CommonResult.underlayError(e.getMessage());
        }
        return CommonResult.failed(e.getErrorCode());
    }

    /**
     * Handle common result.
     *
     * @param e the e
     * @return the common result
     */
    @ResponseBody
    @ExceptionHandler(value = AppException.class)
    public CommonResult<?> handle(AppException e) {
        if (e.getErrorCode() == null) {
            return CommonResult.underlayError(e.getMessage());
        }
        return CommonResult.failed(e.getErrorCode());
    }

    /**
     * Handle valid exception common result.
     *
     * @param e the e
     * @return the common result
     */
    @ResponseBody
    @ExceptionHandler(value = {MethodArgumentNotValidException.class, IllegalArgumentException.class, BindException.class})
    public CommonResult<?> handleValidException(MethodArgumentNotValidException e) {
        var bindingResult = e.getBindingResult();
        String message = null;
        if (bindingResult.hasErrors()) {
            FieldError fieldError = bindingResult.getFieldError();
            if (fieldError != null) {
                message = fieldError.getField() + fieldError.getDefaultMessage();
            }
        }
        return CommonResult.requestError(message);
    }

    /**
     * Handle common result.
     *
     * @param e the e
     * @return the common result
     */
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public CommonResult<?> handle(Exception e) {
        log.error("found unexpected server exception!", e);
        return CommonResult.failed(e.getMessage());
    }
}
