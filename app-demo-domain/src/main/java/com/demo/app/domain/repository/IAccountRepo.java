package com.demo.app.domain.repository;


import com.demo.app.domain.common.entity.CommonPage;
import com.demo.app.domain.common.entity.CommonPageInfo;
import com.demo.app.domain.model.account.Account;

import java.util.Optional;

/**
 * The interface Account repo.
 */
public interface IAccountRepo {
    /**
     * Find by id account.
     *
     * @param id the id
     * @return the account
     */
    Optional<Account> findById(Long id);

    /**
     * Exists by email boolean.
     *
     * @param email the email
     * @return the boolean
     */
    boolean existsByEmail(String email);

    /**
     * Save.
     *
     * @param account the account
     * @return the account
     */
    Account save(Account account);

    /**
     * Find with cards common page.
     *
     * @param page      the page
     * @param withCards the with cards
     * @return the common page
     */
    CommonPage<Account> getPage(CommonPageInfo page, boolean withCards);


    /**
     * Exists card boolean.
     *
     * @param id the id
     * @return the boolean
     */
    boolean existsAccount(Long id);
}
