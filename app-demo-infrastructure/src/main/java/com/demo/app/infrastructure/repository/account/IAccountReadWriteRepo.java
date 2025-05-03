package com.demo.app.infrastructure.repository.account;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.demo.app.domain.common.entity.CommonPageInfo;
import com.demo.app.domain.repository.IAccountRepo;


/**
 * The interface Account read write repo.
 */
public interface IAccountReadWriteRepo extends IAccountRepo {
    /**
     * Find with cards common page.
     *
     * @param page      the page
     * @param withCards the with cards
     * @return the common page
     */
    Page<AccountDO> getPage(CommonPageInfo page, boolean withCards);
}
