package com.demo.app.unit;

import com.demo.app.domain.model.card.CardStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * The type Card status test.
 */
class CardStatusTest {

    /**
     * Test can transition to.
     *
     * @param source         the source
     * @param target         the target
     * @param expectedResult the expected result
     * @param shouldText     the should text
     */
    @ParameterizedTest(name = "{0} should {2}transition to {1}")
    @MethodSource("provideStatusTransitions")
    @DisplayName("Test valid status transitions")
    void testCanTransitionTo(CardStatus source, CardStatus target, boolean expectedResult, String shouldText) {
        assertThat(source.canTransitionTo(target))
                .as("%s -> %s transition should be %s", source, target, expectedResult)
                .isEqualTo(expectedResult);
    }

    private static Stream<Arguments> provideStatusTransitions() {
        return Stream.of(
                // CREATED transitions
                Arguments.of(CardStatus.CREATED, CardStatus.ASSIGNED, true, ""),
                Arguments.of(CardStatus.CREATED, CardStatus.ACTIVATED, false, "not "),
                Arguments.of(CardStatus.CREATED, CardStatus.DEACTIVATED, false, "not "),
                Arguments.of(CardStatus.CREATED, CardStatus.CREATED, false, "not "),
                // ASSIGNED transitions
                Arguments.of(CardStatus.ASSIGNED, CardStatus.ACTIVATED, true, ""),
                Arguments.of(CardStatus.ASSIGNED, CardStatus.DEACTIVATED, true, ""),
                Arguments.of(CardStatus.ASSIGNED, CardStatus.CREATED, false, "not "),
                Arguments.of(CardStatus.ASSIGNED, CardStatus.ASSIGNED, false, "not "),
                // ACTIVATED transitions
                Arguments.of(CardStatus.ACTIVATED, CardStatus.DEACTIVATED, true, ""),
                Arguments.of(CardStatus.ACTIVATED, CardStatus.CREATED, false, "not "),
                Arguments.of(CardStatus.ACTIVATED, CardStatus.ASSIGNED, false, "not "),
                Arguments.of(CardStatus.ACTIVATED, CardStatus.ACTIVATED, false, "not "),
                // DEACTIVATED transitions
                Arguments.of(CardStatus.DEACTIVATED, CardStatus.ACTIVATED, true, ""),
                Arguments.of(CardStatus.DEACTIVATED, CardStatus.CREATED, false, "not "),
                Arguments.of(CardStatus.DEACTIVATED, CardStatus.ASSIGNED, false, "not "),
                Arguments.of(CardStatus.DEACTIVATED, CardStatus.DEACTIVATED, false, "not ")
        );
    }

    /**
     * Enum should have four values.
     */
    @Test
    @DisplayName("Enum should have exactly 4 values")
    void enumShouldHaveFourValues() {
        assertThat(CardStatus.values())
                .hasSize(4)
                .containsExactlyInAnyOrder(
                        CardStatus.CREATED,
                        CardStatus.ASSIGNED,
                        CardStatus.ACTIVATED,
                        CardStatus.DEACTIVATED
                );
    }
}