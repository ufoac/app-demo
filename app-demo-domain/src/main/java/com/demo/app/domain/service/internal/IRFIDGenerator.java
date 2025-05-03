package com.demo.app.domain.service.internal;

import com.demo.app.domain.model.card.RFID;

/**
 * The interface Contract id generator.
 */
public interface IRFIDGenerator {
    /**
     * Generate contract id.
     *
     * @param srcInfo the src info
     * @return the contract id
     */
    RFID generate(Object srcInfo);
}