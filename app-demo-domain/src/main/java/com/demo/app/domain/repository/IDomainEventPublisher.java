package com.demo.app.domain.repository;

import com.demo.app.domain.common.event.DomainEvent;

import java.util.Collection;

/**
 * The interface Domain event publisher.
 */
public interface IDomainEventPublisher {
    /**
     * Publish.
     *
     * @param event the event
     */
    void publish(DomainEvent event);

    /**
     * Publish all.
     *
     * @param events the events
     */
    void publishAll(Collection<DomainEvent> events);
}