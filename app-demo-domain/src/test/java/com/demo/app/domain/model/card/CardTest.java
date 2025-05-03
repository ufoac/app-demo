package com.demo.app.domain.model.card;

import com.demo.app.domain.common.exception.DomainException;
import com.demo.app.domain.model.card.event.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static com.demo.app.domain.common.exception.DomainErrorCode.ILLEGAL_STATE;
import static com.demo.app.domain.model.card.CardStatus.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;

/**
 * The type Card test.
 */
@DisplayName("Card Unit Tests")
class CardTest {
    private static final String CARD_INFO = "Demo Card";
    private static final Long ACCOUNT_ID = 1001L;
    private static final RFID VALID_RFID = new RFID("A1B2C3D4E5F6G7H8", "CDEMO12345");

    /**
     * The type Creation tests.
     */
    @Nested
    @DisplayName("Creation")
    class CreationTests {
        /**
         * Create card.
         */
        @Test
        @DisplayName("should initialize with created status")
        void createCard() {
            var card = Card.create(CARD_INFO);
            assertThat(card).extracting(Card::getInfo, Card::getStatus)
                    .containsExactly(CARD_INFO, CREATED);
            assertThat(card.getDomainEvents()).singleElement()
                    .isInstanceOfSatisfying(CardCreatedEvent.class,
                            e -> assertThat(e.getInfo()).isEqualTo(CARD_INFO));
        }
    }

    /**
     * The type Status transition tests.
     */
    @Nested
    @DisplayName("Status Transitions")
    class StatusTransitionTests {
        private Card card;

        /**
         * Init card.
         */
        @BeforeEach
        void initCard() {
            card = Card.create(CARD_INFO);
            card.clearEvents();
        }

        /**
         * Same status.
         */
        @Test
        @DisplayName("maintain status when same state")
        void sameStatus() {
            assertThat(card.changeStatus(CREATED)).isEqualTo(CREATED);
            assertThat(card.getDomainEvents()).isEmpty();
        }

        /**
         * Invalid transition.
         */
        @Test
        @DisplayName("reject invalid transition")
        void invalidTransition() {
            assertThatThrownBy(() -> card.changeStatus(ACTIVATED))
                    .isInstanceOf(DomainException.class)
                    .hasFieldOrPropertyWithValue("errorCode", ILLEGAL_STATE);
        }

        /**
         * Valid transitions.
         *
         * @param from the from
         * @param to   the to
         */
        @ParameterizedTest(name = "{0}→{1}")
        @CsvSource({"ASSIGNED, ACTIVATED",
                "ASSIGNED, DEACTIVATED",
                "ACTIVATED, DEACTIVATED",
                "DEACTIVATED, ACTIVATED"})
        @DisplayName("allow valid transitions")
        void validTransitions(CardStatus from, CardStatus to) {
            card.Assign(ACCOUNT_ID);
            if (from != ASSIGNED) card.changeStatus(from);
            assertThatCode(() -> card.changeStatus(to)).doesNotThrowAnyException();
        }
    }

    /**
     * The type Assignment tests.
     */
    @Nested
    @DisplayName("Assignment")
    class AssignmentTests {
        private Card card;

        /**
         * Init card.
         */
        @BeforeEach
        void initCard() {
            card = Card.create(CARD_INFO);
            card.clearEvents();
        }

        /**
         * Assign account.
         */
        @Test
        @DisplayName("assign account with events")
        void assignAccount() {
            card.Assign(ACCOUNT_ID);
            assertThat(card).extracting(Card::getAccountId, Card::getStatus)
                    .containsExactly(ACCOUNT_ID, ASSIGNED);
            assertThat(card.getDomainEvents()).satisfiesExactly(
                    e -> assertThat(e).isInstanceOfSatisfying(CardStatusChangedEvent.class,
                            ev -> assertThat(ev.getNewStatus()).isEqualTo(ASSIGNED)),
                    e -> assertThat(e).isInstanceOfSatisfying(CardAssignChangedEvent.class,
                            ev -> assertThat(ev.getNewAccountId()).isEqualTo(ACCOUNT_ID))
            );
        }

        /**
         * Same account.
         */
        @Test
        @DisplayName("skip event on same account")
        void sameAccount() {
            card.Assign(ACCOUNT_ID);
            card.clearEvents();
            card.Assign(ACCOUNT_ID);
            assertThat(card.getDomainEvents()).isEmpty();
        }
    }

    /**
     * The type Rfid tests.
     */
    @Nested
    @DisplayName("RFID")
    class RfidTests {
        /**
         * Valid rfid.
         */
        @Test
        @DisplayName("store valid RFID")
        void validRfid() {
            var card = Card.create(CARD_INFO);
            card.setRfid(VALID_RFID);
            assertThat(card.getRfid()).extracting(RFID::uid, RFID::visibleNumber)
                    .containsExactly(VALID_RFID.uid(), VALID_RFID.visibleNumber());
        }

        /**
         * Invalid rfid.
         */
        @Test
        @DisplayName("reject invalid formats")
        void invalidRfid() {
            assertAll(
                    () -> assertThatThrownBy(() -> RFID.of("invalid", "CDEMO12345"))
                            .isInstanceOf(DomainException.class),
                    () -> assertThatThrownBy(() -> RFID.of(VALID_RFID.uid(), "INVALID"))
                            .isInstanceOf(DomainException.class)
            );
        }
    }
}