package com.demo.app.infrastructure.common.convertor;

import com.demo.app.domain.model.account.Account;
import com.demo.app.domain.model.card.Card;
import com.demo.app.infrastructure.repository.account.AccountDO;
import com.demo.app.infrastructure.repository.card.CardDO;
import org.mapstruct.Mapper;


/**
 * The interface Global convertor.
 */
@Mapper(componentModel = "spring",
        uses = ContractIdConverter.class)
public interface EntityConvertor {
    /**
     * To bo account.
     *
     * @param accountDO the account do
     * @return the account
     */
    Account toBO(AccountDO accountDO);

    /**
     * To do account do.
     *
     * @param account the account
     * @return the account do
     */
    AccountDO toDO(Account account);

    /**
     * To bo card.
     *
     * @param cardDO the card do
     * @return the card
     */
    Card toBO(CardDO cardDO);


    /**
     * To do card do.
     *
     * @param account the account
     * @return the card do
     */
    CardDO toDO(Card account);
}
