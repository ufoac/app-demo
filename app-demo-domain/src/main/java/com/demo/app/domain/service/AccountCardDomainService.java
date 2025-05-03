package com.demo.app.domain.service;

import com.demo.app.domain.common.exception.DomainErrorCode;
import com.demo.app.domain.common.exception.DomainException;
import com.demo.app.domain.model.account.Account;
import com.demo.app.domain.model.account.AccountStatus;
import com.demo.app.domain.model.card.Card;
import com.demo.app.domain.service.internal.IContractIdGenerator;
import com.demo.app.domain.service.internal.IRFIDGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


/**
 * The type Account card domain service.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class AccountCardDomainService {
    private final IContractIdGenerator contractIdGenerator;
    private final IRFIDGenerator rfidGenerator;

    /**
     * Create account.
     *
     * @param email the email
     * @param info  the info
     * @return the account
     */
    public Account createAccount(String email, String info) {
        var account = Account.create(email, info);
        // Temporary mock contract ID generation
        account.setContractId(contractIdGenerator.generate(info));
        return account;
    }

    /**
     * Create card.
     *
     * @param info the info
     * @return the card
     */
    public Card createCard(String info) {
        var card = Card.create(info);
        // Temporary mock RFID generation
        card.setRfid(rfidGenerator.generate(info));
        return card;
    }

    /**
     * Assign.
     *
     * @param account the account
     * @param card    the card
     */
    public void AssignCard(Account account, Card card) {
        if (account.getStatus() == AccountStatus.DEACTIVATED) {
            log.error("Cannot assign a card to deactivated account!");
            throw new DomainException(DomainErrorCode.DEACTIVATED_STATE);
        }
        card.Assign(account.getId());
    }

}
