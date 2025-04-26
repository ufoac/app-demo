package com.demo.app.domain.service;

import com.demo.app.domain.model.contract.ContractId;

/**
 * The interface Contract id generator.
 */
public interface IContractIdGenerator {
    /**
     * Generate contract id.
     *
     * @return the contract id
     */
    ContractId generate(Object srcInfo);
}