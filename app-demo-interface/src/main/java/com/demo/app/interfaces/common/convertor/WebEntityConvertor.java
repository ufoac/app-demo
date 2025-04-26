package com.demo.app.interfaces.common.convertor;

import com.demo.app.domain.model.account.Account;
import com.demo.app.domain.model.card.Card;
import com.demo.app.interfaces.controller.account.response.AccountVO;
import com.demo.app.interfaces.controller.card.response.CardVO;
import org.mapstruct.Mapper;


/**
 * The interface Global convertor.
 */
@Mapper(componentModel = "spring")
public interface WebEntityConvertor {

    /**
     * To bo account.
     *
     * @param accountDTO the account dto
     * @return the account
     */
    AccountVO toVO(Account accountDTO);

    /**
     * To vo card vo.
     *
     * @param cardDTO the card dto
     * @return the card vo
     */
    CardVO toVO(Card cardDTO);
}
