package com.demo.app.domain.service.internal.impl;

import com.demo.app.domain.model.card.RFID;
import com.demo.app.domain.service.internal.IRFIDGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

/**
 * The type Time based contract id generator.
 */
@Service
@RequiredArgsConstructor
public class SnowflakeRFIDGenerator implements IRFIDGenerator {
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private static final char[] VISIBLE_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
    private static final int VISIBLE_CHARS_LEN = VISIBLE_CHARS.length;

    @Value("${MACHINE_ID:100}")
    private long machineId;
    private final SnowflakeIDGenerator snowflake = new SnowflakeIDGenerator(machineId);

    @Override
    public RFID generate(Object srcInfo) {
        // Ignore srcInfo (temporary placeholder)
        // Generate RFID based on mock srcInfo
        return RFID.of(snowflake.generateSnowflakeUID(), generateVisibleValue());
    }

    private static String generateVisibleValue() {
        StringBuilder sb = new StringBuilder(10);
        for (int i = 0; i < 10; i++) {
            sb.append(VISIBLE_CHARS[SECURE_RANDOM.nextInt(VISIBLE_CHARS_LEN)]);
        }
        return sb.toString();
    }

    /**
     * The type Snowflake id generator.
     */
    static class SnowflakeIDGenerator {
        private final long machineId; // 机器ID (0-31)
        private long sequence = 0L;
        private long lastTimestamp = -1L;
        private static final long SEQUENCE_BITS = 12;
        private static final long MACHINE_ID_SHIFT = SEQUENCE_BITS;
        private static final long TIMESTAMP_SHIFT = SEQUENCE_BITS + 5;
        private static final long MAX_SEQUENCE = ~(-1L << SEQUENCE_BITS);

        /**
         * Instantiates a new Snowflake rfid generator.
         *
         * @param machineId the machine id
         */
        public SnowflakeIDGenerator(long machineId) {
            this.machineId = machineId & 0x1F; // 确保机器ID在0-31之间
        }

        /**
         * Generate snowflake uid string.
         *
         * @return the string
         */
        public synchronized String generateSnowflakeUID() {
            long timestamp = System.currentTimeMillis();
            if (timestamp < lastTimestamp) {
                throw new RuntimeException("时钟回拨异常");
            }

            if (timestamp == lastTimestamp) {
                sequence = (sequence + 1) & MAX_SEQUENCE;
                if (sequence == 0) {
                    timestamp = tilNextMillis(lastTimestamp);
                }
            } else {
                sequence = 0L;
            }

            lastTimestamp = timestamp;

            long id = (timestamp << TIMESTAMP_SHIFT)
                    | (machineId << MACHINE_ID_SHIFT)
                    | sequence;

            return String.format("%016X", id);
        }

        private long tilNextMillis(long lastTimestamp) {
            long timestamp = System.currentTimeMillis();
            while (timestamp <= lastTimestamp) {
                timestamp = System.currentTimeMillis();
            }
            return timestamp;
        }
    }
}