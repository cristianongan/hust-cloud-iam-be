package com.hust.common.util;

import java.security.SecureRandom;
import org.apache.commons.text.RandomStringGenerator;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UUIDUtil {
    private static final String UPPERCASE_ALPHA_NUMERIC = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    public static String generateUUID(int count, String digits) {
        RandomStringGenerator referenceGenerator = new RandomStringGenerator.Builder()
                .selectFrom(digits.toCharArray())
                .usingRandom(new SecureRandom()::nextInt)
                .get();
        return referenceGenerator.generate(count);
    }

    public static String generateUUID(int count) {
        return generateUUID(count, UPPERCASE_ALPHA_NUMERIC);
    }
}
