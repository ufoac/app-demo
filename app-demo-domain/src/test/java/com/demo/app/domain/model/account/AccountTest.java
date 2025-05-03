package com.demo.app.domain.model.account;

import com.demo.app.domain.common.exception.DomainException;
import com.demo.app.domain.model.account.event.AccountCreatedEvent;
import com.demo.app.domain.model.account.event.AccountStatusChangedEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static com.demo.app.domain.common.exception.DomainErrorCode.ILLEGAL_STATE;
import static com.demo.app.domain.model.account.AccountStatus.*;
import static org.assertj.core.api.Assertions.*;

/**
 * The type Account test.
 */
@DisplayName("Account Unit Tests")
class AccountTest {
    private static final String TEST_EMAIL = "user@demo.com";
    private static final String ACCOUNT_INFO = "Demo Account";
    /**
     * The type Creation tests.
     */
    @Nested
    @DisplayName("Creation")
    class CreationTests {
        /**
         * Create account.
         */
        @Test
        @DisplayName("create with initial state and event")
        void createAccount() {
            var account = Account.create(TEST_EMAIL, ACCOUNT_INFO);

            assertThat(account).extracting(Account::getEmail, Account::getInfo, Account::getStatus)
                    .containsExactly(TEST_EMAIL, ACCOUNT_INFO, CREATED);
            assertThat(account.getDomainEvents()).singleElement()
                    .isInstanceOfSatisfying(AccountCreatedEvent.class,
                            e -> assertThat(e.getEmail()).isEqualTo(TEST_EMAIL));
        }
    }

    /**
     * The type Status transition tests.
     */
    @Nested
    @DisplayName("Status Transitions")
    class StatusTransitionTests {
        private Account account;

        /**
         * Init account.
         */
        @BeforeEach
        void initAccount() {
            account = Account.create(TEST_EMAIL, ACCOUNT_INFO);
            account.clearEvents();
        }

        /**
         * Same status.
         */
        @Test
        @DisplayName("maintain same status")
        void sameStatus() {
            assertThat(account.changeStatus(CREATED)).isEqualTo(CREATED);
            assertThat(account.getDomainEvents()).isEmpty();
        }

        /**
         * Valid transitions.
         *
         * @param from the from
         * @param to   the to
         */
        @ParameterizedTest(name = "{0} → {1}")
        @CsvSource({"CREATED, ACTIVATED",
                "CREATED, DEACTIVATED",
                "DEACTIVATED, ACTIVATED"})
        @DisplayName("allow valid transitions")
        void validTransitions(AccountStatus from, AccountStatus to) {
            if (from != CREATED) {
                account.changeStatus(from);
                account.clearEvents();
            }
            assertThatCode(() -> account.changeStatus(to)).doesNotThrowAnyException();
            assertThat(account.getStatus()).isEqualTo(to);
            assertThat(account.getDomainEvents()).singleElement()
                    .extracting("newStatus").isEqualTo(to);
        }

        /**
         * Invalid transitions.
         *
         * @param from the from
         * @param to   the to
         */
        @ParameterizedTest(name = "{0} → {1}")
        @CsvSource({"ACTIVATED, CREATED",
                "DEACTIVATED, CREATED"})
        @DisplayName("block invalid transitions")
        void invalidTransitions(AccountStatus from, AccountStatus to) {
            account.changeStatus(from);
            account.clearEvents();
            assertThatThrownBy(() -> account.changeStatus(to))
                    .isInstanceOf(DomainException.class)
                    .hasFieldOrPropertyWithValue("errorCode", ILLEGAL_STATE);
            assertThat(account).returns(from, Account::getStatus)
                    .returns(List.of(), Account::getDomainEvents);
        }
    }

    /**
     * The type Contract id tests.
     */
    @Nested
    @DisplayName("Contract ID")
    class ContractIdTests {
        /**
         * Invalid contract ids.
         *
         * @param invalidValue the invalid value
         */
        @ParameterizedTest(name = "invalid: {0}")
        @ValueSource(strings = {"", "EUTTT123", "EUCON12345678901X"})
        @DisplayName("reject invalid formats")
        void invalidContractIds(String invalidValue) {
            assertThatThrownBy(() -> ContractId.of(invalidValue))
                    .isInstanceOf(DomainException.class);
        }
    }

    /**
     * The type Event tests.
     */
    @Nested
    @DisplayName("Events")
    class EventTests {
        private Account account;

        /**
         * Init account.
         */
        @BeforeEach
        void initAccount() {
            account = Account.create(TEST_EMAIL, ACCOUNT_INFO);
            account.clearEvents();
        }

        /**
         * Publish events.
         */
        @Test
        @DisplayName("publish status change events")
        void publishEvents() {
            account.changeStatus(ACTIVATED);
            account.changeStatus(DEACTIVATED);
            assertThat(account.getDomainEvents())
                    .extracting(e -> ((AccountStatusChangedEvent) e).getOldStatus(),
                            e -> ((AccountStatusChangedEvent) e).getNewStatus())
                    .containsExactly(tuple(CREATED, ACTIVATED), tuple(ACTIVATED, DEACTIVATED));
        }

        /**
         * No event on same status.
         */
        @Test
        @DisplayName("skip event on same status")
        void noEventOnSameStatus() {
            var newStatus = account.changeStatus(CREATED);
            assertThat(newStatus).isEqualTo(CREATED);
            assertThat(account.getDomainEvents()).isEmpty();
        }
    }
}