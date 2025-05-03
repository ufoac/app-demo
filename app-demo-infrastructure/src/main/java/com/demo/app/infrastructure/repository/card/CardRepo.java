package com.demo.app.infrastructure.repository.card;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.demo.app.domain.common.entity.CommonPageInfo;
import com.demo.app.domain.model.card.Card;
import com.demo.app.infrastructure.common.convertor.EntityConvertor;
import com.demo.app.infrastructure.common.exception.InfrastructureException;
import com.demo.app.infrastructure.repository.card.mapper.CardMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.demo.app.infrastructure.common.exception.InfrastructureErrorCode.OPTIMISTIC_LOCKER_FAILED;


/**
 * The type Card repo.
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class CardRepo implements ICardReadWriteRepo {
    private final CardMapper cardMapper;
    private final EntityConvertor entityConvertor;

    @Override
    public Page<CardDO> findByAccountId(Long accountId, CommonPageInfo pageInfo) {
        var query = new LambdaQueryWrapper<CardDO>()
                .eq(CardDO::getAccountId, accountId)
                .orderByDesc(CardDO::getUpdateTime);
        var page = new Page<CardDO>(pageInfo.getPageNum(), pageInfo.getPageSize());
        return cardMapper.selectPage(page, query);
    }

    @Override
    public Card save(Card card) {
        CardDO cardDO = entityConvertor.toDO(card);
        var success = cardMapper.insertOrUpdate(cardDO);
        if (!success) {
            log.error("Save card error, optimistic lock conflict detected");
            throw new InfrastructureException(OPTIMISTIC_LOCKER_FAILED);
        }
        return entityConvertor.toBO(cardDO);
    }

    @Override
    public void changeStatus(Card card) {
        var updateCardDO = new CardDO();
        updateCardDO.setId(card.getId());
        updateCardDO.setStatus(card.getStatus());
        updateCardDO.setVersion(card.getVersion());
        var updated = cardMapper.updateById(updateCardDO);
        if (updated <= 0) {
            log.error("Save card error, optimistic lock conflict detected");
            throw new InfrastructureException(OPTIMISTIC_LOCKER_FAILED);
        }
    }

    @Override
    public void changeAccount(Card card) {
        var updateCardDO = new CardDO();
        updateCardDO.setId(card.getId());
        updateCardDO.setStatus(card.getStatus());
        updateCardDO.setAccountId(card.getAccountId());
        updateCardDO.setVersion(card.getVersion());
        var updated = cardMapper.updateById(updateCardDO);
        if (updated <= 0) {
            log.error("Save card error, optimistic lock conflict detected");
            throw new InfrastructureException(OPTIMISTIC_LOCKER_FAILED);
        }
    }

    @Override
    public Optional<Card> findById(Long id) {
        return Optional.ofNullable(cardMapper.selectById(id))
                .map(entityConvertor::toBO);
    }

    @Override
    public boolean existsCard(Long id) {
        return cardMapper.exists(new LambdaQueryWrapper<CardDO>()
                .eq(CardDO::getId, id));
    }

}
