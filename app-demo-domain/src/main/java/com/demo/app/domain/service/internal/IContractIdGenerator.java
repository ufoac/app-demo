package com.demo.app.domain.service.internal;

import com.demo.app.domain.model.account.ContractId;

/**
 * The interface Contract id generator.
 */
public interface IContractIdGenerator {
    /**
     * Generate contract id.
     *
     * @param srcInfo the src info
     * @return the contract id
     */
    ContractId generate(Object srcInfo);
}