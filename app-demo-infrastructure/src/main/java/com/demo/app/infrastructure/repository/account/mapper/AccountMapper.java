package com.demo.app.infrastructure.repository.account.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.demo.app.infrastructure.repository.account.AccountDO;
import org.apache.ibatis.annotations.Mapper;


/**
 * The interface Account mapper.
 */
@Mapper
public interface AccountMapper extends BaseMapper<AccountDO> {
}
