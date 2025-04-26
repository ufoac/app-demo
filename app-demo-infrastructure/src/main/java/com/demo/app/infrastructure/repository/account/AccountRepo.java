package com.demo.app.infrastructure.repository.account;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.demo.app.domain.common.entity.CommonPage;
import com.demo.app.domain.common.entity.CommonPageInfo;
import com.demo.app.domain.model.account.Account;
import com.demo.app.domain.repository.IAccountRepo;
import com.demo.app.infrastructure.common.convertor.EntityConvertor;
import com.demo.app.infrastructure.common.convertor.PageConvertor;
import com.demo.app.infrastructure.common.exception.InfrastructureException;
import com.demo.app.infrastructure.repository.account.mapper.AccountMapper;
import com.demo.app.infrastructure.repository.card.CardDO;
import com.demo.app.infrastructure.repository.card.mapper.CardMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.demo.app.infrastructure.common.exception.InfrastructureErrorCode.OPTIMISTIC_LOCKER_FAILED;


/**
 * The type Account repo.
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class AccountRepo implements IAccountRepo {
    private final AccountMapper accountMapper;
    private final CardMapper cardMapper;

    private final EntityConvertor entityConvertor;

    @Override
    public Account save(Account account) {
        AccountDO accountDO = entityConvertor.toDO(account);
        var success = accountMapper.insertOrUpdate(accountDO);
        if (!success) {
            log.error("Save account error, optimistic lock conflict detected");
            throw new InfrastructureException(OPTIMISTIC_LOCKER_FAILED);
        }
        return entityConvertor.toBO(accountDO);
    }

    @Override
    public boolean existsByEmail(String email) {
        var query = new LambdaQueryWrapper<AccountDO>();
        query.eq(AccountDO::getEmail, email);
        return accountMapper.exists(query);
    }

    @Override
    public Optional<Account> findById(Long id) {
        return Optional.ofNullable(accountMapper.selectById(id))
                .map(entityConvertor::toBO);
    }

    @Override
    public CommonPage<Account> getPage(CommonPageInfo page, boolean withCards) {
        Page<AccountDO> pageInfo = new Page<>(
                page.getPageNum(),
                page.getPageSize()
        );
        var resultPage = accountMapper.selectPage(pageInfo, new LambdaQueryWrapper<AccountDO>()
                .orderByDesc(AccountDO::getUpdateTime));
        if (withCards && !resultPage.getRecords().isEmpty()) {
            // Use two separate queries to avoid the N+1 problem while ensuring pagination accuracy
            var accountIds = resultPage.getRecords().stream()
                    .map(AccountDO::getId)
                    .toList();
            var cardsMap = cardMapper.selectList(
                            new LambdaQueryWrapper<CardDO>()
                                    .in(CardDO::getAccountId, accountIds)
                                    .orderByDesc(CardDO::getUpdateTime)
                    ).stream()
                    .collect(Collectors.groupingBy(CardDO::getAccountId));
            if (!cardsMap.isEmpty()) {
                resultPage.getRecords().forEach(account ->
                        account.setCards(cardsMap.getOrDefault(account.getId(), Collections.emptyList()))
                );
            }
        }
        return PageConvertor.toCommonPage(resultPage, entityConvertor::toBO);
    }

    @Override
    public boolean existsAccount(Long id) {
        return accountMapper.exists(new LambdaQueryWrapper<AccountDO>()
                .eq(AccountDO::getId, id));
    }
}
