package com.demo.app.infrastructure.repository.card;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.demo.app.domain.common.entity.CommonPageInfo;
import com.demo.app.domain.repository.ICardRepo;


/**
 * The interface Card read write repo.
 */
public interface ICardReadWriteRepo extends ICardRepo {
    /**
     * Find by account id list.
     *
     * @param accountId the account id
     * @param pageInfo  the page info
     * @return the list
     */
    Page<CardDO> findByAccountId(Long accountId, CommonPageInfo pageInfo);
}
