package com.demo.app.infrastructure.event;

import com.demo.app.domain.common.entity.AggregateRoot;
import com.demo.app.domain.common.event.DomainEvent;
import com.demo.app.domain.repository.IDomainEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * The type Spring domain event publisher.
 */
@Component
@RequiredArgsConstructor
public class SpringDomainEventPublisher implements IDomainEventPublisher {
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void publish(DomainEvent event) {
        applicationEventPublisher.publishEvent(event);
    }

    @Override
    public <ID> void pollEventFrom(AggregateRoot<ID> root) {
        var events = root.getEvents();
        events.forEach(applicationEventPublisher::publishEvent);
        root.clearEvents();
    }
}