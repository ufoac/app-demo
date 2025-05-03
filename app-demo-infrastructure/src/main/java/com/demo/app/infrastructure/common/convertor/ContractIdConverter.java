package com.demo.app.infrastructure.common.convertor;

import com.demo.app.domain.model.account.ContractId;

/**
 * The type Contract id converter.
 */
public class ContractIdConverter {
    /**
     * String to contract id contract .
     *
     * @param value the value
     * @return the contract id
     */
    public static ContractId stringToContractId(String value) {
        return value != null ? new ContractId(value) : null;
    }

    /**
     * Contract id to string .
     *
     * @param contractId the contract id
     * @return the string
     */
    public static String contractIdToString(ContractId contractId) {
        return contractId != null ? contractId.toString() : null;
    }
}