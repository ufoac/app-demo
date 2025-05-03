
package com.demo.app.domain.model.card.event;

import com.demo.app.domain.common.event.DomainEvent;
import lombok.Getter;
import lombok.experimental.SuperBuilder;


/**
 * The type Account status changed event.
 */
@Getter
@SuperBuilder
public class CardCreatedEvent extends DomainEvent {

    private final String info;

    /**
     * Instantiates a new Account created event.
     *
     * @param info the info
     */
    public CardCreatedEvent(String info) {
        this.info = info;
    }
}
