package com.demo.app.domain.service.internal.impl;

import com.demo.app.domain.model.card.RFID;
import com.demo.app.domain.service.internal.IRFIDGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

/**
 * The type Time based contract id generator.
 */
@Service
@RequiredArgsConstructor
public class RandomRFIDGenerator implements IRFIDGenerator {
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private static final char[] VISIBLE_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
    private static final int VISIBLE_CHARS_LEN = VISIBLE_CHARS.length;

    @Override
    public RFID generate(Object srcInfo) {
        // Ignore srcInfo (temporary placeholder)
        // Generate RFID based on mock srcInfo
        return RFID.of(generateUID(), generateVisibleValue());
    }

    private static String generateUID() {
        byte[] bytes = new byte[8];
        SECURE_RANDOM.nextBytes(bytes);
        return bytesToHex(bytes);
    }

    /**
     * Bytes to hex string.
     *
     * @param bytes the bytes
     * @return the string
     */
    public static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X", b & 0xFF));
        }
        return sb.toString();
    }

    private static String generateVisibleValue() {
        StringBuilder sb = new StringBuilder(10);
        for (int i = 0; i < 10; i++) {
            sb.append(VISIBLE_CHARS[SECURE_RANDOM.nextInt(VISIBLE_CHARS_LEN)]);
        }
        return sb.toString();
    }
}