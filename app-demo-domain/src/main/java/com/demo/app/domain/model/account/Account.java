package com.demo.app.domain.model.account;


import com.demo.app.domain.common.entity.AggregateRoot;
import com.demo.app.domain.common.exception.DomainException;
import com.demo.app.domain.model.account.event.AccountCreatedEvent;
import com.demo.app.domain.model.account.event.AccountStatusChangedEvent;
import com.google.common.base.Strings;
import lombok.*;

import static com.demo.app.domain.common.exception.DomainErrorCode.ILLEGAL_STATE;
import static com.demo.app.domain.common.exception.DomainErrorCode.VALIDATION_EMAIL_FAILED;

/**
 * The type Account.
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Account extends AggregateRoot<Long> {
    private String email;
    private AccountStatus status;
    private ContractId contractId;
    private String info;

    /**
     * Create account .
     *
     * @param email the email
     * @param info  the info
     * @return the account
     */
    public static Account create(String email, String info) {
        if (Strings.isNullOrEmpty(email)) {
            throw new DomainException(VALIDATION_EMAIL_FAILED);
        }
        var account = new Account();
        account.email = email;
        account.info = info;
        account.status = AccountStatus.CREATED;
        account.registerEvent(new AccountCreatedEvent(email));
        return account;
    }

    /**
     * Change status.
     *
     * @param newStatus the new status
     * @return the account status
     */
    public AccountStatus changeStatus(AccountStatus newStatus) {
        if (this.status == newStatus) {
            return this.status;
        }
        if (!this.status.canTransitionTo(newStatus)) {
            throw new DomainException(ILLEGAL_STATE);
        }
        registerEvent(new AccountStatusChangedEvent(id, status, newStatus));
        this.status = newStatus;
        return this.status;
    }
}