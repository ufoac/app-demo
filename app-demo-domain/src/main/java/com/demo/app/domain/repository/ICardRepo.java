package com.demo.app.domain.repository;

import com.demo.app.domain.common.entity.CommonPage;
import com.demo.app.domain.common.entity.CommonPageInfo;
import com.demo.app.domain.model.card.Card;

import java.util.Optional;

/**
 * The interface Card repo.
 */
public interface ICardRepo {
    /**
     * Find by id card.
     *
     * @param id the id
     * @return the card
     */
    Optional<Card> findById(Long id);

    /**
     * Find by account id list.
     *
     * @param accountId the account id
     * @param pageInfo  the page info
     * @return the list
     */
    CommonPage<Card> findByAccountId(Long accountId, CommonPageInfo pageInfo);

    /**
     * Save.
     *
     * @param card the card
     * @return the card
     */
    Card save(Card card);


    /**
     * Exists card boolean.
     *
     * @param id the id
     * @return the boolean
     */
    boolean existsCard(Long id);
}
