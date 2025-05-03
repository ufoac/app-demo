package com.demo.app.infrastructure.repository.account;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.demo.app.domain.common.entity.CommonPageInfo;
import com.demo.app.domain.model.account.Account;
import com.demo.app.domain.model.account.AccountStatus;
import com.demo.app.infrastructure.common.convertor.EntityConvertor;
import com.demo.app.infrastructure.common.exception.InfrastructureException;
import com.demo.app.infrastructure.repository.account.mapper.AccountMapper;
import com.demo.app.infrastructure.repository.card.CardDO;
import com.demo.app.infrastructure.repository.card.mapper.CardMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.demo.app.infrastructure.common.exception.InfrastructureErrorCode.OPTIMISTIC_LOCKER_FAILED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * The type Account repo test.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Account Repository Unit Tests")
class AccountRepoTest {

    @Mock
    private AccountMapper accountMapper;
    @Mock
    private CardMapper cardMapper;
    @Spy
    private EntityConvertor entityConvertor = Mappers.getMapper(EntityConvertor.class);
    @InjectMocks
    private AccountRepo accountRepo;

    private Account testAccount;
    private AccountDO testAccountDO;

    private static class TestData {
        /**
         * Create account account.
         *
         * @return the account
         */
        static Account createAccount() {
            Account account = new Account();
            account.setId(1L);
            account.setEmail("test@demo.com");
            account.setStatus(AccountStatus.CREATED);
            account.setVersion(1);
            return account;
        }

        /**
         * Create account do account do.
         *
         * @return the account do
         */
        static AccountDO createAccountDO() {
            AccountDO accountDO = new AccountDO();
            accountDO.setId(1L);
            accountDO.setEmail("test@demo.com");
            accountDO.setStatus(AccountStatus.CREATED);
            accountDO.setVersion(1);
            return accountDO;
        }

        /**
         * Create cards list.
         *
         * @param count the count
         * @return the list
         */
        static List<CardDO> createCards(int count) {
            List<CardDO> cards = new ArrayList<>();
            for (int i = 1; i <= count; i++) {
                CardDO card = new CardDO();
                card.setId(1000L + i);
                card.setAccountId(1L);
                cards.add(card);
            }
            return cards;
        }
    }

    /**
     * Sets .
     */
    @BeforeEach
    void setup() {
        testAccount = TestData.createAccount();
        testAccountDO = TestData.createAccountDO();
    }

    /**
     * Save account.
     */
    @Test
    @DisplayName("Save account with version control")
    void saveAccount() {
        // Stub insert operation
        when(accountMapper.insertOrUpdate(any(AccountDO.class))).thenReturn(true);

        Account result = accountRepo.save(testAccount);

        assertThat(result.getId()).isEqualTo(1L);
        verify(accountMapper).insertOrUpdate(argThat((AccountDO data) ->
                data.getVersion() == 1 &&
                        data.getEmail().equals("test@demo.com")
        ));
    }

    /**
     * Save account failure.
     */
    @Test
    @DisplayName("Throw on optimistic lock failure")
    void saveAccount_Failure() {
        when(accountMapper.insertOrUpdate((AccountDO) any())).thenReturn(false);

        assertThatThrownBy(() -> accountRepo.save(testAccount))
                .isInstanceOf(InfrastructureException.class)
                .hasFieldOrPropertyWithValue("errorCode", OPTIMISTIC_LOCKER_FAILED);
    }

    /**
     * Update status.
     */
    @Test
    @DisplayName("Update account status")
    void updateStatus() {
        testAccount.setStatus(AccountStatus.DEACTIVATED);

        when(accountMapper.updateById((AccountDO) argThat((AccountDO data) ->
                data.getStatus() == AccountStatus.DEACTIVATED
        ))).thenReturn(1);

        accountRepo.changeStatus(testAccount);

        verify(accountMapper).updateById(argThat((AccountDO data) ->
                data.getVersion() == 1 &&
                        data.getId() == 1L
        ));
    }

    /**
     * Find by id exists.
     */
    @Test
    @DisplayName("Find account by existing ID")
    void findById_Exists() {
        when(accountMapper.selectById(1L)).thenReturn(testAccountDO);

        Optional<Account> result = accountRepo.findById(1L);

        assertThat(result)
                .isPresent()
                .get()
                .returns("test@demo.com", Account::getEmail);
    }

    /**
     * Gets page with cards.
     */
    @Test
    @DisplayName("Load paged results with cards")
    void getPageWithCards() {
        // Setup paged accounts
        Page<AccountDO> mockPage = new Page<>(1, 10, 1);
        mockPage.setRecords(List.of(testAccountDO));

        // Setup related cards
        List<CardDO> testCards = TestData.createCards(3);
        when(cardMapper.selectList(any(LambdaQueryWrapper.class)))
                .thenReturn(testCards);

        when(accountMapper.selectPage(any(), any())).thenReturn(mockPage);

        Page<AccountDO> result = accountRepo.getPage(new CommonPageInfo(1L, 10L), true);

        assertThat(result.getRecords())
                .hasSize(1)
                .first()
                .satisfies(account ->
                        assertThat(account.getCards())
                                .hasSize(3)
                                .allMatch(card -> card.getAccountId() == 1L)
                );
    }
}