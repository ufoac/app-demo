package com.demo.app.domain.model.card;


import com.demo.app.domain.common.event.DomainEvent;
import com.demo.app.domain.common.exception.DomainException;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static com.demo.app.domain.common.exception.DomainErrorCode.ILLEGAL_STATE;

/**
 * The type Card.
 */
@Data
@NoArgsConstructor
public class Card {
    private Long id;
    private String contractId;
    private Long accountId;
    private CardStatus status;
    private String info;
    private Integer version;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    /**
     * Instantiates a new Card.
     *
     * @param info the info
     */
    public Card(String info) {
        this.info = info;
        this.status = CardStatus.CREATED;
    }


    /**
     * Change status.
     *
     * @param newStatus the new status
     * @return the list
     */
    public List<DomainEvent> changeStatus(CardStatus newStatus) {
        if (!this.status.canTransitionTo(newStatus)) {
            throw new DomainException(ILLEGAL_STATE);
        }
        var event = new CardStatusChangedEvent(id, status, newStatus);
        this.status = newStatus;
        return List.of(event);
    }

    /**
     * Assign.
     *
     * @param accountId the account id
     * @return the list
     */
    public List<DomainEvent> Assign(Long accountId) {
        DomainEvent event = null;
        if (this.status == CardStatus.CREATED) {
            this.status = CardStatus.ASSIGNED;
            event = new CardStatusChangedEvent(id, CardStatus.CREATED, CardStatus.ASSIGNED);
        }
        this.accountId = accountId;
        return event == null ? Collections.emptyList() : List.of(event);
    }
}