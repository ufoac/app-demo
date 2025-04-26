package com.demo.app.domain.model.card;

import com.demo.app.domain.common.event.DomainEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


/**
 * The type Account status changed event.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardStatusChangedEvent extends DomainEvent {
    private Long cardId;
    private CardStatus oldStatus;
    private CardStatus newStatus;
}
