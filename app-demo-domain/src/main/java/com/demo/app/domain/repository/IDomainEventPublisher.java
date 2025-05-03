package com.demo.app.domain.repository;

import com.demo.app.domain.common.entity.AggregateRoot;
import com.demo.app.domain.common.event.DomainEvent;

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
     * Poll event from.
     *
     * @param <ID> the type parameter
     * @param root the root
     */
    <ID> void pollEventFrom(AggregateRoot<ID> root);
}