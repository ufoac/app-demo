package com.demo.app.infrastructure.event;

import com.demo.app.domain.common.event.DomainEvent;
import com.demo.app.domain.repository.IDomainEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.Collection;

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
    public void publishAll(Collection<DomainEvent> events) {
        events.forEach(applicationEventPublisher::publishEvent);
    }
}