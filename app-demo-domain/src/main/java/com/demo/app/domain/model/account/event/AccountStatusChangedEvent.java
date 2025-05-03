package com.demo.app.domain.model.account.event;

import com.demo.app.domain.common.event.DomainEvent;
import com.demo.app.domain.model.account.AccountStatus;
import lombok.*;


/**
 * The type Account status changed event.
 */
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
public class AccountStatusChangedEvent extends DomainEvent {

    private Long accountId;
    private AccountStatus oldStatus;
    private AccountStatus newStatus;

    /**
     * Instantiates a new Account status changed event.
     *
     * @param accountId the account id
     * @param oldStatus the old status
     * @param newStatus the new status
     */
    public AccountStatusChangedEvent(Long accountId,
                                     AccountStatus oldStatus,
                                     AccountStatus newStatus) {
        this.accountId = accountId;
        this.newStatus = newStatus;
        this.oldStatus = oldStatus;
    }
}
