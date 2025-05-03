package com.demo.app.domain.repository;


import com.demo.app.domain.model.account.Account;

import java.util.Optional;

/**
 * The interface Account repo.
 */
public interface IAccountRepo {


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
     * Find by id account.
     *
     * @param id the id
     * @return the account
     */
    Optional<Account> findById(Long id);
    /**
     * Change status.
     *
     * @param account the account
     */
    void changeStatus(Account account);


    /**
     * Exists card boolean.
     *
     * @param id the id
     * @return the boolean
     */
    boolean existsAccount(Long id);
}
