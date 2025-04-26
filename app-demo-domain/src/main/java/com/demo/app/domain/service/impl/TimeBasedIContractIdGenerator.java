package com.demo.app.domain.service.impl;

import com.demo.app.domain.model.contract.ContractId;
import com.demo.app.domain.service.IContractIdGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * The type Time based contract id generator.
 */
@Service
@RequiredArgsConstructor
public class TimeBasedIContractIdGenerator implements IContractIdGenerator {
    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("yyMMddHHmmssSSS");
    private static final String COUNTRY_CODE = "CN";
    private static final String PROVIDER_CODE = "VOV";
    private static final int SEQ_LEN = 9;
    private static final int RADIX = 36;
    private static final int CHECKSUM_WEIGHT_MOD = 37;
    /**
     * Generate contract id.
     *
     * @return the contract id
     */
    @Override
    public ContractId generate(Object srcInfo) {
        // Ignore srcInfo (temporary placeholder)
        // Retrieve country/provider code from mock srcInfo
        // Generate time-based unique sequence part (Base36 encoded) + Random(1)
        String uniquePart = generateUniquePart();
        // Compose EMAID format
        return ContractId.of(COUNTRY_CODE + PROVIDER_CODE + uniquePart);
    }

    private String generateUniquePart() {
        var now = LocalDateTime.now();
        var timePart = Long.parseLong(now.format(DATE_FORMATTER));
        var base36Time = Long.toString(timePart, RADIX).toUpperCase();
        var prefix = padToLength(base36Time, SEQ_LEN);
        var checksum = calculateWeightedChecksum(prefix);
        return prefix + Character.toUpperCase(checksum);
    }

    private String padToLength(String input, int length) {
        return input.length() >= length
                ? input.substring(0, length)
                : String.format("%-" + length + "s", input).replace(' ', '0');
    }

    private char calculateWeightedChecksum(String input) {
        var sum = 0;
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            int value = Character.digit(c, RADIX);
            sum += value;
        }
        return Character.forDigit(sum % CHECKSUM_WEIGHT_MOD % RADIX, RADIX);
    }
}