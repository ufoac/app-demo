package com.demo.app.service.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.demo.app.domain.common.entity.CommonPageInfo;
import com.demo.app.domain.model.account.Account;
import com.demo.app.domain.model.account.AccountStatus;
import com.demo.app.domain.model.card.Card;
import com.demo.app.domain.model.card.CardStatus;
import com.demo.app.domain.repository.IDomainEventPublisher;
import com.demo.app.domain.service.AccountCardDomainService;
import com.demo.app.infrastructure.repository.account.AccountDO;
import com.demo.app.infrastructure.repository.account.IAccountReadWriteRepo;
import com.demo.app.infrastructure.repository.card.CardDO;
import com.demo.app.infrastructure.repository.card.ICardReadWriteRepo;
import com.demo.app.service.common.convertor.AppEntityConvertor;
import com.demo.app.service.common.entity.request.*;
import com.demo.app.service.common.entity.response.AccountDTO;
import com.demo.app.service.common.entity.response.CardDTO;
import com.demo.app.service.common.exception.AppException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.demo.app.service.common.exception.AppErrorCode.EMAIL_DUPLICATED;
import static com.demo.app.service.common.exception.AppErrorCode.NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * The type Account card biz service test.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("AccountCardBizService Unit Tests")
class AccountCardBizServiceTest {
    private static final Long ACCOUNT_ID = 1L;
    private static final Long CARD_ID = 100L;
    private static final String VALID_EMAIL = "user@demo.com";
    private static final String ACCOUNT_INFO = "Test Account";
    private static final String CARD_INFO = "Test Card";

    @Mock
    private IAccountReadWriteRepo accountRepo;
    @Mock
    private ICardReadWriteRepo cardRepo;
    @Mock
    private IDomainEventPublisher eventPublisher;
    @Mock
    private AccountCardDomainService domainService;
    @Spy
    private AppEntityConvertor appEntityConvertor = Mappers.getMapper(AppEntityConvertor.class);
    @InjectMocks
    private AccountCardBizService service;

    private CreateAccountCommand createAccountCmd() {
        return CreateAccountCommand.builder()
                .email(VALID_EMAIL)
                .info(ACCOUNT_INFO)
                .build();
    }

    private UpdateAccountCommand updateAccountCmd(AccountStatus status) {
        return UpdateAccountCommand.builder()
                .id(ACCOUNT_ID)
                .status(status)
                .build();
    }

    /**
     * The type Account operations.
     */
    @Nested
    @DisplayName("Account Operations")
    class AccountOperations {
        /**
         * Create account success.
         */
        @Test
        @DisplayName("create account successfully")
        void createAccountSuccess() {
            when(accountRepo.existsByEmail(VALID_EMAIL)).thenReturn(false);
            when(domainService.createAccount(any(), any())).thenReturn(mock(Account.class));

            service.createAccount(createAccountCmd());

            verify(accountRepo).save(any());
            verify(eventPublisher).pollEventFrom(any());
        }

        /**
         * Reject duplicate email.
         */
        @Test
        @DisplayName("reject duplicate email account")
        void rejectDuplicateEmail() {
            when(accountRepo.existsByEmail(VALID_EMAIL)).thenReturn(true);

            assertThatThrownBy(() -> service.createAccount(createAccountCmd()))
                    .isInstanceOf(AppException.class)
                    .hasFieldOrPropertyWithValue("errorCode", EMAIL_DUPLICATED);
        }

        /**
         * Update account status.
         */
        @Test
        @DisplayName("update account status to activated")
        void updateAccountStatus() {
            var account = mock(Account.class);
            when(accountRepo.findById(ACCOUNT_ID)).thenReturn(Optional.of(account));
            when(account.changeStatus(AccountStatus.ACTIVATED)).thenReturn(AccountStatus.ACTIVATED);

            var result = service.updateAccountStatus(updateAccountCmd(AccountStatus.ACTIVATED));

            assertThat(result).isEqualTo(AccountStatus.ACTIVATED);
            verify(accountRepo).changeStatus(account);
        }
    }

    /**
     * The type Card operations.
     */
    @Nested
    @DisplayName("Card Operations")
    class CardOperations {
        private CreateCardCommand createCardCmd() {
            return CreateCardCommand.builder().info(CARD_INFO).build();
        }

        private UpdateCardCommand updateCardCmd(CardStatus status) {
            return UpdateCardCommand.builder()
                    .id(CARD_ID)
                    .status(status)
                    .build();
        }

        /**
         * Create card success.
         */
        @Test
        @DisplayName("create new card successfully")
        void createCardSuccess() {
            when(domainService.createCard(any())).thenReturn(mock(Card.class));

            service.createCard(createCardCmd());

            verify(cardRepo).save(any());
            verify(eventPublisher).pollEventFrom(any());
        }

        /**
         * Activate card.
         */
        @Test
        @DisplayName("activate card status")
        void activateCard() {
            var card = mock(Card.class);
            when(cardRepo.findById(CARD_ID)).thenReturn(Optional.of(card));
            when(card.changeStatus(CardStatus.ACTIVATED)).thenReturn(CardStatus.ACTIVATED);

            var result = service.updateCardStatus(updateCardCmd(CardStatus.ACTIVATED));

            assertThat(result).isEqualTo(CardStatus.ACTIVATED);
            verify(cardRepo).changeStatus(card);
        }
    }

    /**
     * The type Card assignment.
     */
    @Nested
    @DisplayName("Card Assignment")
    class CardAssignment {
        private AssignCardCommand assignCmd() {
            return AssignCardCommand.builder()
                    .accountId(ACCOUNT_ID)
                    .cardId(CARD_ID)
                    .build();
        }

        /**
         * Assign card success.
         */
        @Test
        @DisplayName("assign card to account")
        void assignCardSuccess() {
            when(accountRepo.findById(ACCOUNT_ID)).thenReturn(Optional.of(mock(Account.class)));
            when(cardRepo.findById(CARD_ID)).thenReturn(Optional.of(mock(Card.class)));

            service.assignCard(assignCmd());

            verify(domainService).AssignCard(any(), any());
            verify(cardRepo).changeAccount(any());
        }

        /**
         * Assign to invalid account.
         */
        @Test
        @DisplayName("fail assignment with invalid account")
        void assignToInvalidAccount() {
            when(cardRepo.findById(CARD_ID)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.assignCard(assignCmd()))
                    .isInstanceOf(AppException.class)
                    .hasFieldOrPropertyWithValue("errorCode", NOT_FOUND);
        }
    }

    /**
     * The type Query operations.
     */
    @Nested
    @DisplayName("Query Operations")
    class QueryOperations {
        private final CommonPageInfo pageInfo = new CommonPageInfo(1L, 10L);

        private <T> Page<T> mockMybatisPage(List<T> data) {
            Page<T> page = new Page<>(pageInfo.getPageNum(), pageInfo.getPageSize());
            page.setRecords(data);
            page.setTotal(100L);
            return page;
        }

        /**
         * Gets account page.
         */
        @Test
        @DisplayName("get account page with conversion")
        void getAccountPage() {
            var mybatisPage = mockMybatisPage(Collections.singletonList(new AccountDO()));
            when(accountRepo.getPage(any(), anyBoolean())).thenReturn(mybatisPage);
            when(appEntityConvertor.toDTO(any(AccountDO.class))).thenReturn(new AccountDTO());

            var result = service.getAccountPage(pageInfo, true);

            assertThat(result).isNotNull();
            assertThat(result.getList()).hasSize(1);
            verify(accountRepo).getPage(pageInfo, true);
        }

        /**
         * Gets card by account.
         */
        @Test
        @DisplayName("get card page by account")
        void getCardByAccount() {
            var mybatisPage = mockMybatisPage(Collections.singletonList(new CardDO()));
            when(cardRepo.findByAccountId(anyLong(), any())).thenReturn(mybatisPage);
            when(appEntityConvertor.toDTO(any(CardDO.class))).thenReturn(new CardDTO());

            var result = service.getCardByAccountId(ACCOUNT_ID, pageInfo);

            assertThat(result).isNotNull();
            assertThat(result.getList()).hasSize(1);
            verify(cardRepo).findByAccountId(ACCOUNT_ID, pageInfo);
        }
    }
}