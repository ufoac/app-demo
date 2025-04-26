package com.demo.app.domain.model.contract;

import com.demo.app.domain.common.exception.DomainException;
import lombok.extern.slf4j.Slf4j;

import static com.demo.app.domain.common.exception.DomainErrorCode.VALIDATION_EMAID_FAILED;


/**
 * The type Contract id.
 */
@Slf4j
public record ContractId(String value) {
    /**
     * Of contract id.
     *
     * @param value the value
     * @return the contract id
     */
    public static ContractId of(String value) {
        var ret = validateFormat(value);
        if (!ret) {
            log.error("invalid contract id {}", value);
            throw new DomainException(VALIDATION_EMAID_FAILED);
        }
        return new ContractId(value);
    }

    /**
     * Validate format boolean.
     *
     * @param value the value
     * @return the boolean
     */
    public static boolean validateFormat(String value) {
        return value.matches("(?i)^[A-Z]{2}[A-Z0-9]{3}[A-Z0-9]{9}[A-Z0-9]?$");
    }

}