package com.demo.app.domain.common.event;


import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * The type Domain event.
 */
@Getter
@SuperBuilder
public abstract class DomainEvent {
    private final String eventId;
    private final LocalDateTime occurredOn;

    /**
     * Instantiates a new Domain event.
     */
    protected DomainEvent() {
        this.eventId = UUID.randomUUID().toString();
        this.occurredOn = LocalDateTime.now();
    }
}