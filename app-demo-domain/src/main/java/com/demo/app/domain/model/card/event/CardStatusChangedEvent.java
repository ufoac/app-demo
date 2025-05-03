package com.demo.app.domain.model.card.event;

import com.demo.app.domain.common.event.DomainEvent;
import com.demo.app.domain.model.card.CardStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


/**
 * The type Account status changed event.
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CardStatusChangedEvent extends DomainEvent {
    private Long cardId;
    private CardStatus oldStatus;
    private CardStatus newStatus;

    /**
     * Instantiates a new Card status changed event.
     *
     * @param cardId    the card id
     * @param oldStatus the old status
     * @param newStatus the new status
     */
    public CardStatusChangedEvent(Long cardId, CardStatus oldStatus,
                                  CardStatus newStatus) {
        this.cardId = cardId;
        this.oldStatus = oldStatus;
        this.newStatus = newStatus;
    }
}