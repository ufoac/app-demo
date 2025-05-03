package com.demo.app.domain.model.card;

import com.demo.app.domain.common.exception.DomainException;
import lombok.extern.slf4j.Slf4j;

import java.util.regex.Pattern;

import static com.demo.app.domain.common.exception.DomainErrorCode.VALIDATION_RFID_FAILED;

/**
 * The type Rfid.
 */
@Slf4j
public record RFID(String uid, String visibleNumber) {
    private static final Pattern UID_PATTERN = Pattern.compile("^[A-F0-9]{16}$");
    private static final Pattern VISIBLE_VALUE_PATTERN = Pattern.compile("^[A-Z0-9]{10}$");

    /**
     * Instantiates a new Rfid.
     *
     * @param uid           the uid
     * @param visibleNumber the visible number
     * @return the rfid
     */
    public static RFID of(String uid, String visibleNumber) {
        if (!isValidUID(uid)) {
            log.error("RFID UID format error: {}", uid);
            throw new DomainException(VALIDATION_RFID_FAILED);
        }
        if (!isValidVisibleValue(visibleNumber)) {
            log.error("RFID VisibleValue format error: {}", visibleNumber);
            throw new DomainException(VALIDATION_RFID_FAILED);
        }
        return new RFID(uid, visibleNumber);
    }

    /**
     * 校验 UID 格式
     */
    private static boolean isValidUID(String uid) {
        return uid != null
                && UID_PATTERN.matcher(uid).matches();
    }

    /**
     * 校验 VisibleValue 格式
     */
    private static boolean isValidVisibleValue(String visibleValue) {
        return visibleValue != null
                && VISIBLE_VALUE_PATTERN.matcher(visibleValue).matches();
    }
}