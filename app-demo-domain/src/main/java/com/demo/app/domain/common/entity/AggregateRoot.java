package com.demo.app.domain.common.entity;

import com.demo.app.domain.common.event.DomainEvent;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * The type Aggregate root.
 *
 * @param <ID> the type parameter
 */
@Data
public abstract class AggregateRoot<ID> {
    /**
     * The Id.
     */
    protected ID id;
    /**
     * The Version.
     */
    protected Integer version = 0;
    /**
     * The Creation time.
     */
    protected LocalDateTime createTime;
    /**
     * The Update time.
     */
    protected LocalDateTime updateTime;

    private volatile List<DomainEvent> domainEvents;

    /**
     * Register event.
     *
     * @param event the event
     */
    protected void registerEvent(DomainEvent event) {
        if (domainEvents == null) {
            domainEvents = new ArrayList<>();
        }
        domainEvents.add(event);
    }

    /**
     * Gets domain events.
     *
     * @return the domain events
     */
    public List<DomainEvent> getEvents() {
        return domainEvents == null ? Collections.emptyList() : domainEvents;
    }

    /**
     * Clear domain events.
     */
    public void clearEvents() {
        if (domainEvents != null) {
            domainEvents.clear();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AggregateRoot<?> that = (AggregateRoot<?>) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
