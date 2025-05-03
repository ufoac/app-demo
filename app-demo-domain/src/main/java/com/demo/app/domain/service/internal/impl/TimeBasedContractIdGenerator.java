package com.demo.app.domain.service.internal.impl;

import com.demo.app.domain.model.account.ContractId;
import com.demo.app.domain.service.internal.IContractIdGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * The type Time based contract id generator.
 */
@Service
@RequiredArgsConstructor
public class TimeBasedContractIdGenerator implements IContractIdGenerator {
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
        // Generate ContractId based on mock srcInfo
        String uniquePart = generateUniquePart();
        var prefix = COUNTRY_CODE + PROVIDER_CODE + uniquePart;
        var checkSum = calculateWeightedChecksum(prefix);
        return ContractId.of(prefix + checkSum);
    }

    private String generateUniquePart() {
        var now = LocalDateTime.now();
        var timePart = Long.parseLong(now.format(DATE_FORMATTER));
        return padToLength(Long.toString(timePart, RADIX).toUpperCase(), SEQ_LEN);
    }

    private String padToLength(String input, int length) {
        return input.length() >= length
                ? input.substring(0, length)
                : String.format("%-" + length + "s", input).replace(' ', '0');
    }

    /**
     * Calculate weighted checksum char.
     *
     * @param input the input
     * @return the char
     */
    public static char calculateWeightedChecksum(String input) {
        var sum = 0;
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            int value = Character.digit(c, RADIX);
            sum += value;
        }
        return Character.toUpperCase(Character.forDigit(sum % CHECKSUM_WEIGHT_MOD % RADIX, RADIX));
    }
}