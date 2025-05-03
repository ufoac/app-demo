package com.demo.app.domain.service;

import com.demo.app.domain.common.exception.DomainException;
import com.demo.app.domain.model.account.Account;
import com.demo.app.domain.model.account.AccountStatus;
import com.demo.app.domain.model.card.Card;
import com.demo.app.domain.service.internal.impl.SnowflakeRFIDGenerator;
import com.demo.app.domain.service.internal.impl.TimeBasedContractIdGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.demo.app.domain.common.exception.DomainErrorCode.DEACTIVATED_STATE;
import static com.demo.app.domain.common.exception.DomainErrorCode.VALIDATION_EMAIL_FAILED;
import static com.demo.app.domain.model.card.CardStatus.ASSIGNED;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.mock;

/**
 * The type Account card domain service test.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Account Card Domain Service Unit Tests")
class AccountCardDomainServiceTest {
    private static final String TEST_EMAIL = "test@test.com";
    private static final String ACCOUNT_INFO = "test account";
    private static final String CARD_INFO = "test card";
    private static final int CONTRACT_ID_LENGTH = 15;
    private static final String CONTRACT_ID_REGEX = "[A-Z0-9]{15}";
    private static final int RFID_UID_LENGTH = 16;
    private static final int VISIBLE_NUMBER_LENGTH = 10;

    private AccountCardDomainService service;

    /**
     * Sets .
     */
    @BeforeEach
    void setup() {
        service = new AccountCardDomainService(
                new TimeBasedContractIdGenerator(),
                new SnowflakeRFIDGenerator()
        );
    }

    /**
     * The type Account creation tests.
     */
    @Nested
    @DisplayName("Account Creation")
    class AccountCreationTests {

        /**
         * Should generate valid contract id.
         */
        @Test
        @DisplayName("generates valid contract ID with checksum")
        void shouldGenerateValidContractId() {
            var account = service.createAccount(TEST_EMAIL, ACCOUNT_INFO);
            var contractId = account.getContractId().value();

            assertThat(contractId)
                    .hasSize(CONTRACT_ID_LENGTH)
                    .matches(CONTRACT_ID_REGEX);
            var uniquePart = contractId.substring(0, 14);
            var expectedChecksum = TimeBasedContractIdGenerator.calculateWeightedChecksum(uniquePart);
            assertThat(contractId.charAt(14)).isEqualTo(expectedChecksum);
        }
    }

    /**
     * The type Card creation tests.
     */
    @Nested
    @DisplayName("Card Creation")
    class CardCreationTests {

        /**
         * Should generate valid rfid.
         */
        @Test
        @DisplayName("generates valid RFID with formatted numbers")
        void shouldGenerateValidRfid() {
            var card = service.createCard(CARD_INFO);
            var rfid = card.getRfid();

            assertThat(rfid.uid())
                    .hasSize(RFID_UID_LENGTH)
                    .matches("^[A-F0-9]{16}$");
            assertThat(rfid.visibleNumber())
                    .hasSize(VISIBLE_NUMBER_LENGTH)
                    .matches("^[A-Z0-9]{10}$");
        }
    }

    /**
     * The type Card assignment tests.
     */
    @Nested
    @DisplayName("Card Assignment")
    class CardAssignmentTests {
        private Account activeAccount;
        private Account deactivatedAccount;
        private Card card;

        /**
         * Sets .
         */
        @BeforeEach
        void setup() {
            activeAccount = service.createAccount("active@demo.com", ACCOUNT_INFO);
            activeAccount.setId(1L);

            deactivatedAccount = service.createAccount("deactivated@demo.com", ACCOUNT_INFO);
            deactivatedAccount.changeStatus(AccountStatus.DEACTIVATED);

            card = service.createCard(CARD_INFO);
            card.clearEvents();
        }

        /**
         * Should assign to active account.
         */
        @Test
        @DisplayName("assigns card to active account successfully")
        void shouldAssignToActiveAccount() {
            assertThatCode(() -> service.AssignCard(activeAccount, card))
                    .doesNotThrowAnyException();

            assertThat(card.getAccountId()).isEqualTo(activeAccount.getId());
            assertThat(card.getStatus()).isEqualTo(ASSIGNED);
        }

        /**
         * Should reject deactivated account.
         */
        @Test
        @DisplayName("throws exception when assigning to deactivated account")
        void shouldRejectDeactivatedAccount() {
            assertThatThrownBy(() -> service.AssignCard(deactivatedAccount, card))
                    .isInstanceOf(DomainException.class)
                    .hasFieldOrPropertyWithValue("errorCode", DEACTIVATED_STATE);
        }
    }

    /**
     * The type Validation tests.
     */
    @Nested
    @DisplayName("Validation Rules")
    class ValidationTests {

        /**
         * Should enforce parameter validation.
         */
        @Test
        @DisplayName("rejects null parameters with proper error codes")
        void shouldEnforceParameterValidation() {
            assertThatThrownBy(() -> service.createAccount(null, ACCOUNT_INFO))
                    .isInstanceOf(DomainException.class)
                    .hasFieldOrPropertyWithValue("errorCode", VALIDATION_EMAIL_FAILED);

            assertThatThrownBy(() -> service.AssignCard(null, mock(Card.class)))
                    .isInstanceOf(NullPointerException.class);
        }
    }
}