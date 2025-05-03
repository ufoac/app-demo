package com.demo.app.domain.model.card.event;

import com.demo.app.domain.common.event.DomainEvent;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * CardAssignChanedEvent
 *
 * @author cao
 * @since 2025 /5/1
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class CardAssignChangedEvent extends DomainEvent {

    private Long cardId;
    private Long oldAccountId;
    private Long newAccountId;

    /**
     * Instantiates a new Card assign changed event.
     *
     * @param cardId       the card id
     * @param oldAccountId the old account id
     * @param newAccountId the new account id
     */
    public CardAssignChangedEvent(Long cardId,
                                  Long oldAccountId,
                                  Long newAccountId) {
        super();
        this.cardId = cardId;
        this.oldAccountId = oldAccountId;
        this.newAccountId = newAccountId;
    }
}
