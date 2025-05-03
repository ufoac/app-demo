package com.demo.app.service.service;

import com.demo.app.domain.common.entity.CommonPage;
import com.demo.app.domain.common.entity.CommonPageInfo;
import com.demo.app.domain.model.account.Account;
import com.demo.app.domain.model.account.AccountStatus;
import com.demo.app.domain.model.card.Card;
import com.demo.app.domain.model.card.CardStatus;
import com.demo.app.domain.repository.IDomainEventPublisher;
import com.demo.app.domain.service.AccountCardDomainService;
import com.demo.app.infrastructure.common.convertor.PageConvertor;
import com.demo.app.infrastructure.repository.account.IAccountReadWriteRepo;
import com.demo.app.infrastructure.repository.card.ICardReadWriteRepo;
import com.demo.app.service.common.convertor.AppEntityConvertor;
import com.demo.app.service.common.entity.request.*;
import com.demo.app.service.common.entity.response.AccountDTO;
import com.demo.app.service.common.entity.response.CardDTO;
import com.demo.app.service.common.exception.AppErrorCode;
import com.demo.app.service.common.exception.AppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.demo.app.service.common.exception.AppErrorCode.EMAIL_DUPLICATED;


/**
 * The type Account app service.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AccountCardBizService {
    private final IAccountReadWriteRepo accountRepo;
    private final ICardReadWriteRepo cardRepo;
    private final IDomainEventPublisher domainEventPublisher;
    private final AccountCardDomainService accountCardDomainService;
    private final AppEntityConvertor appEntityConvertor;

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
        var account = accountCardDomainService.createAccount(command.getEmail(), command.getInfo());
        domainEventPublisher.pollEventFrom(account);
        return accountRepo.save(account);
    }

    /**
     * Update account status account.
     * Retry for
     *
     * @param command the command
     * @return the account status
     */
    @Transactional(rollbackFor = Exception.class)
    public AccountStatus updateAccountStatus(UpdateAccountCommand command) {
        var account = accountRepo.findById(command.getId())
                .orElseThrow(() -> new AppException(AppErrorCode.NOT_FOUND));
        var status = account.changeStatus(command.getStatus());
        // will publish event after transaction committed/rollback
        domainEventPublisher.pollEventFrom(account);
        accountRepo.changeStatus(account);
        return status;
    }


    /**
     * Creat card.
     *
     * @param command the command
     * @return the card
     */
    @Transactional(rollbackFor = Exception.class)
    public Card createCard(CreateCardCommand command) {
        var card = accountCardDomainService.createCard(command.getInfo());
        domainEventPublisher.pollEventFrom(card);
        return cardRepo.save(card);
    }

    /**
     * Update card status card.
     *
     * @param command the command
     * @return the card
     */
    @Transactional(rollbackFor = Exception.class)
    public CardStatus updateCardStatus(UpdateCardCommand command) {
        Card card = cardRepo.findById(command.getId())
                .orElseThrow(() -> new AppException(AppErrorCode.NOT_FOUND));
        var status = card.changeStatus(command.getStatus());
        // will publish event after transaction committed/rollback
        domainEventPublisher.pollEventFrom(card);
        cardRepo.changeStatus(card);
        return status;
    }

    /**
     * Assign card.
     *
     * @param command the command
     */
    @Transactional(rollbackFor = Exception.class)
    public void assignCard(AssignCardCommand command) {
        Card card = cardRepo.findById(command.getCardId())
                .orElseThrow(() -> new AppException(AppErrorCode.NOT_FOUND));
        Account account = accountRepo.findById(command.getAccountId())
                .orElseThrow(() -> new AppException(AppErrorCode.NOT_FOUND));
        accountCardDomainService.AssignCard(account, card);
        domainEventPublisher.pollEventFrom(card);
        cardRepo.changeAccount(card);
    }


    /**
     * Gets account with first page card.
     *
     * @param pageInfo the page info
     * @param withCard the with card
     * @return the account with first page card
     */
    @Transactional(rollbackFor = Exception.class, readOnly = true)
    public CommonPage<AccountDTO> getAccountPage(CommonPageInfo pageInfo, boolean withCard) {
        return PageConvertor.toCommonPage(accountRepo.getPage(pageInfo, withCard), appEntityConvertor::toDTO);
    }

    /**
     * Gets card by account id.
     *
     * @param accountId the account id
     * @param pageInfo  the page info
     * @return the card by account id
     */
    public CommonPage<CardDTO> getCardByAccountId(Long accountId, CommonPageInfo pageInfo) {
        return PageConvertor.toCommonPage(cardRepo.findByAccountId(accountId, pageInfo), appEntityConvertor::toDTO);
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
