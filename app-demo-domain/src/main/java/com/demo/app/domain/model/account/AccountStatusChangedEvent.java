package com.demo.app.domain.model.account;

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
public class AccountStatusChangedEvent extends DomainEvent {
    private Long accountId;
    private AccountStatus oldStatus;
    private AccountStatus newStatus;
}
