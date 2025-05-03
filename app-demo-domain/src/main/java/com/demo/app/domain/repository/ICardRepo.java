package com.demo.app.domain.repository;

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
     * Save.
     *
     * @param card the card
     * @return the card
     */
    Card save(Card card);

    /**
     * Change status.
     *
     * @param card the card
     */
    void changeStatus(Card card);


    /**
     * Change account.
     *
     * @param card the card
     */
    void changeAccount(Card card);

    /**
     * Exists card boolean.
     *
     * @param id the id
     * @return the boolean
     */
    boolean existsCard(Long id);
}
