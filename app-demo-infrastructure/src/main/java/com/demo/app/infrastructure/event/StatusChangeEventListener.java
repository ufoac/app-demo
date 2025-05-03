package com.demo.app.infrastructure.event;

import com.demo.app.domain.model.account.event.AccountStatusChangedEvent;
import com.demo.app.domain.model.card.event.CardStatusChangedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * The type Status change event listener.
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class StatusChangeEventListener {

    /**
     * Handle account status change.
     *
     * @param event the event
     */
    @Async("event-pool")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleAccountStatusChange(AccountStatusChangedEvent event) {
        // could send to email/mq...
        log.info("[EVENT] Account status changed: {} -> {}",
                event.getOldStatus(), event.getNewStatus());

    }

    @Async("event-pool")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
    public void handleAccountStatusChangeError(AccountStatusChangedEvent event) {
        // could send to email/mq...
        log.info("[EVENT-ERROR] Account status changing ERROR : {} >! {}",
                event.getOldStatus(), event.getNewStatus());
    }

    /**
     * Handle card status change.
     *
     * @param event the event
     */
    @Async("event-pool")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleCardStatusChange(CardStatusChangedEvent event) {
        // could send to email/mq...
        log.info("[EVENT] Card status changed: {} -> {}", event.getOldStatus(), event.getNewStatus());
    }

    @Async("event-pool")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
    public void handleCardStatusChangeError(CardStatusChangedEvent event) {
        // could send to email/mq...
        log.info("[EVENT-ERROR] Card status changing Error: {} >! {}", event.getOldStatus(), event.getNewStatus());
    }
}