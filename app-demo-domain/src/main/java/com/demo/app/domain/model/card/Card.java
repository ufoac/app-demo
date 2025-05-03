package com.demo.app.domain.model.card;


import com.demo.app.domain.common.entity.AggregateRoot;
import com.demo.app.domain.common.exception.DomainException;
import com.demo.app.domain.model.card.event.CardAssignChangedEvent;
import com.demo.app.domain.model.card.event.CardCreatedEvent;
import com.demo.app.domain.model.card.event.CardStatusChangedEvent;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Objects;

import static com.demo.app.domain.common.exception.DomainErrorCode.ILLEGAL_STATE;

/**
 * The type Card.
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Card extends AggregateRoot<Long> {
    private Long accountId;
    private CardStatus status;
    private String info;
    private RFID rfid;

    /**
     * Create account.
     *
     * @param info the info
     * @return the account
     */
    public static Card create(String info) {
        var card = new Card();
        card.info = info;
        card.status = CardStatus.CREATED;
        card.registerEvent(new CardCreatedEvent(info));
        return card;
    }


    /**
     * Change status.
     *
     * @param newStatus the new status
     * @return the card status
     */
    public CardStatus changeStatus(CardStatus newStatus) {
        if (this.status == newStatus) {
            return this.status;
        }
        if (this.status == CardStatus.CREATED) {
            throw new DomainException(ILLEGAL_STATE);
        }
        if (!this.status.canTransitionTo(newStatus)) {
            throw new DomainException(ILLEGAL_STATE);
        }
        registerEvent(new CardStatusChangedEvent(id, status, newStatus));
        this.status = newStatus;
        return this.status;
    }

    /**
     * Assign.
     *
     * @param accountId the account id
     */
    public void Assign(Long accountId) {
        if (Objects.equals(this.accountId, accountId)) {
            return;
        }
        if (this.status == CardStatus.CREATED) {
            this.status = CardStatus.ASSIGNED;
            registerEvent(new CardStatusChangedEvent(id, CardStatus.CREATED, CardStatus.ASSIGNED));
        }
        registerEvent(new CardAssignChangedEvent(id, this.accountId, accountId));
        this.accountId = accountId;
    }
}