package com.demo.app.service.common.convertor;

import com.demo.app.infrastructure.repository.account.AccountDO;
import com.demo.app.infrastructure.repository.card.CardDO;
import com.demo.app.service.common.entity.response.AccountDTO;
import com.demo.app.service.common.entity.response.CardDTO;
import org.mapstruct.Mapper;


/**
 * The interface Global convertor.
 */
@Mapper(componentModel = "spring") // 生成 Spring Bean
public interface AppEntityConvertor {
    /**
     * To bo account dto.
     *
     * @param accountDO the account do
     * @return the account dto
     */
    AccountDTO toDTO(AccountDO accountDO);

    /**
     * To dto card dto.
     *
     * @param cardDO the card do
     * @return the card dto
     */
    CardDTO toDTO(CardDO cardDO);
}
