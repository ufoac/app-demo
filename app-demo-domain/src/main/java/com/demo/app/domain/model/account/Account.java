package com.demo.app.domain.model.account;


import com.demo.app.domain.common.event.DomainEvent;
import com.demo.app.domain.common.exception.DomainException;
import com.demo.app.domain.model.card.Card;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

import static com.demo.app.domain.common.exception.DomainErrorCode.ILLEGAL_STATE;

/**
 * The type Account.
 */
@Data
@NoArgsConstructor
public class Account {
    private Long id;
    private String email;
    private String contractId;
    private AccountStatus status;
    private String info;
    private List<Card> cards;
    private Integer version;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    /**
     * Instantiates a new Account.
     *
     * @param email the email
     * @param info  the info
     */
    public Account(String email, String info) {
        this.email = email;
        this.info = info;
        this.status = AccountStatus.CREATED;
    }

    /**
     * Change status.
     *
     * @param newStatus the new status
     * @return the list
     */
    public List<DomainEvent> changeStatus(AccountStatus newStatus) {
        if (!this.status.canTransitionTo(newStatus)) {
            throw new DomainException(ILLEGAL_STATE);
        }
        var event = new AccountStatusChangedEvent(id, status, newStatus);
        this.status = newStatus;
        return List.of(event);

    }
}