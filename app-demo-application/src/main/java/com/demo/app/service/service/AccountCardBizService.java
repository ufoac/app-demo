package com.demo.app.service.service;

import com.demo.app.domain.common.entity.CommonPage;
import com.demo.app.domain.common.entity.CommonPageInfo;
import com.demo.app.domain.model.account.Account;
import com.demo.app.domain.model.card.Card;
import com.demo.app.domain.repository.IAccountRepo;
import com.demo.app.domain.repository.ICardRepo;
import com.demo.app.domain.repository.IDomainEventPublisher;
import com.demo.app.domain.service.IContractIdGenerator;
import com.demo.app.service.common.exception.AppErrorCode;
import com.demo.app.service.common.exception.AppException;
import com.demo.app.service.dto.request.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.demo.app.service.common.exception.AppErrorCode.*;


/**
 * The type Account app service.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AccountCardBizService {
    private final IAccountRepo accountRepo;
    private final ICardRepo cardRepo;
    private final IDomainEventPublisher domainEventPublisher;
    private final IContractIdGenerator contractIdGenerator;

    /**
     * Create account.
     *
     * @param command the command
     * @return the account
     */
    @Transactional(rollbackFor = Exception.class)
    public Account createAccount(CreateAccountCommand command) {
        if (accountRepo.existsByEmail(command.getEmail())) {
            log.error("cannot create account, email already existed! {}", command.getEmail());
            throw new AppException(EMAIL_DUPLICATED);
        }
        var account = new Account(command.getEmail(), command.getInfo());
        account.setContractId(contractIdGenerator.generate(command).value());
        return accountRepo.save(account);
    }

    /**
     * Update account status account.
     * Retry for
     *
     * @param command the command
     * @return the account
     */
    @Transactional(rollbackFor = Exception.class)
    public Account updateAccountStatus(UpdateAccountCommand command) {
        Account account = accountRepo.findById(command.getId())
                .orElseThrow(() -> new AppException(AppErrorCode.NOT_FOUND));
        var update = new Account();
        update.setId(account.getId());
        update.setStatus(account.getStatus());
        update.setVersion(account.getVersion());
        var event = update.changeStatus(command.getStatus());
        // publish after save committed/rollback
        domainEventPublisher.publishAll(event);
        return accountRepo.save(update);
    }


    /**
     * Creat card.
     *
     * @param command the command
     * @return the card
     */
    @Transactional(rollbackFor = Exception.class)
    public Card createCard(CreateCardCommand command) {
        var card = new Card(command.getInfo());
        return cardRepo.save(card);
    }

    /**
     * Update card status card.
     *
     * @param command the command
     * @return the card
     */
    @Transactional(rollbackFor = Exception.class)
    public Card updateCardStatus(UpdateCardCommand command) {
        Card card = cardRepo.findById(command.getId())
                .orElseThrow(() -> new AppException(AppErrorCode.NOT_FOUND));
        var update = new Card();
        update.setId(card.getId());
        update.setStatus(card.getStatus());
        update.setVersion(card.getVersion());
        var event = update.changeStatus(command.getStatus());
        // publish after save committed/rollback
        domainEventPublisher.publishAll(event);
        return cardRepo.save(update);
    }

    /**
     * Assign card.
     *
     * @param command the command
     * @return the card
     */
    @Transactional(rollbackFor = Exception.class)
    public Card assignCard(AssignCardCommand command) {
        Card card = cardRepo.findById(command.getCardId())
                .orElseThrow(() -> new AppException(AppErrorCode.NOT_FOUND));
        if (!accountRepo.existsAccount(command.getAccountId())) {
            throw new AppException(AppErrorCode.NOT_FOUND);
        }
        var update = new Card();
        update.setId(card.getId());
        update.setStatus(card.getStatus());
        update.setVersion(card.getVersion());
        var event = update.Assign(command.getAccountId());
        // publish after save committed/rollback
        domainEventPublisher.publishAll(event);
        return cardRepo.save(update);
    }


    /**
     * Gets account with first page card.
     *
     * @param pageInfo the page info
     * @param withCard the with card
     * @return the account with first page card
     */
    @Transactional(rollbackFor = Exception.class, readOnly = true)
    public CommonPage<Account> getAccountPage(CommonPageInfo pageInfo, boolean withCard) {
        return accountRepo.getPage(pageInfo, withCard);
    }

    /**
     * Gets card by account id.
     *
     * @param accountId the account id
     * @param pageInfo  the page info
     * @return the card by account id
     */
    public CommonPage<Card> getCardByAccountId(Long accountId, CommonPageInfo pageInfo) {
        return cardRepo.findByAccountId(accountId, pageInfo);
    }

    /**
     * Gets account by id.
     *
     * @param id the id
     * @return the account by id
     */
    public Optional<Account> getAccountById(Long id) {
        return accountRepo.findById(id);
    }

    /**
     * Gets card by id.
     *
     * @param id the id
     * @return the card by id
     */
    public Optional<Card> getCardById(Long id) {
        return cardRepo.findById(id);
    }


}
