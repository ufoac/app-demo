package com.demo.app.interfaces.common.convertor;

import com.demo.app.domain.model.account.Account;
import com.demo.app.domain.model.card.Card;
import com.demo.app.infrastructure.common.convertor.ContractIdConverter;
import com.demo.app.interfaces.controller.account.response.AccountVO;
import com.demo.app.interfaces.controller.card.response.CardVO;
import com.demo.app.service.common.entity.response.AccountDTO;
import com.demo.app.service.common.entity.response.CardDTO;
import org.mapstruct.Mapper;


/**
 * The interface Global convertor.
 */
@Mapper(componentModel = "spring",
        uses = ContractIdConverter.class)
public interface WebEntityConvertor {

    /**
     * To bo account.
     *
     * @param account the account
     * @return the account
     */
    AccountVO toVO(Account account);


    /**
     * To dto account vo.
     *
     * @param accountDTO the account dto
     * @return the account vo
     */
    AccountVO toVO(AccountDTO accountDTO);

    /**
     * To vo card vo.
     *
     * @param card the card
     * @return the card vo
     */
    CardVO toVO(Card card);


    /**
     * To vo account vo.
     *
     * @param cardDTO the card dto
     * @return the account vo
     */
    CardVO toVO(CardDTO cardDTO);
}
