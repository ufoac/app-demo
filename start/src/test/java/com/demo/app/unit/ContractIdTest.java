package com.demo.app.unit;

import com.demo.app.domain.common.exception.DomainException;
import com.demo.app.domain.model.contract.ContractId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static com.demo.app.domain.common.exception.DomainErrorCode.VALIDATION_EMAID_FAILED;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * The type Contract id test.
 */
class ContractIdTest {

    /**
     * Should create valid contract id.
     *
     * @param input the input
     */
    @ParameterizedTest(name = "[{index}] {0} should pass validation")
    @ValueSource(strings = {
            "CNVOV123456789",    // Standard 14-digit
            "CNVOVA1B2C3D4EA"    // Optional checksum
    })
    @DisplayName("Validate legal contract ID formats")
    void shouldCreateValidContractId(String input) {
        assertThatCode(() -> ContractId.of(input))
                .doesNotThrowAnyException();
    }

    /**
     * Should reject invalid formats.
     *
     * @param input the input
     */
    @ParameterizedTest(name = "[{index}] {0} should throw exception")
    @ValueSource(strings = {
            "CNVOV123456789444",  // Exceeds max length
            "CNVOV-23+56/89",     // Illegal characters
            ""                    // Empty value
    })
    @DisplayName("Reject illegal contract ID formats")
    void shouldRejectInvalidFormats(String input) {
        assertThatThrownBy(() -> ContractId.of(input))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining(VALIDATION_EMAID_FAILED.getMessage());
    }
}