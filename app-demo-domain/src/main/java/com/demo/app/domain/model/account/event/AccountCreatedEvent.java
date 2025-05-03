package com.demo.app.domain.model.account.event;

import com.demo.app.domain.common.event.DomainEvent;
import lombok.Getter;
import lombok.experimental.SuperBuilder;


/**
 * The type Account status changed event.
 */
@Getter
@SuperBuilder
public class AccountCreatedEvent extends DomainEvent {

    private final String email;

    /**
     * Instantiates a new Account created event.
     *
     * @param email the email
     */
    public AccountCreatedEvent(String email) {
        this.email = email;
    }
}
