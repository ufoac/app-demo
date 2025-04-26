package com.demo.app.domain.common.event;


import java.time.LocalDateTime;

/**
 * The type Domain event.
 */
public abstract class DomainEvent {
    private final LocalDateTime time = LocalDateTime.now();
}
