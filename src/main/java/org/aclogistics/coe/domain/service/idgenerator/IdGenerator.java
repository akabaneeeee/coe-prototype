package org.aclogistics.coe.domain.service.idgenerator;

import com.google.common.primitives.UnsignedBytes;
import java.security.SecureRandom;

/**
 * @author Rosendo Coquilla
 */
public abstract class IdGenerator {

    private static final int MAX_RANDOM_CHAR_LENGTH = 8;
    private static final String POOL = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final SecureRandom secureRandom = new SecureRandom();

    private static String generateDefaultId(int length) {
        StringBuilder sb = new StringBuilder(length);

        byte[] randomBytes = new byte[length];
        secureRandom.nextBytes(randomBytes);

        for (int i = 0; i < length; i++) {
            sb.append(POOL.charAt(UnsignedBytes.toInt(randomBytes[i]) % POOL.length()));
        }

        return sb.toString();
    }

    public static String generate(String prefix) {
        return prefix + generateDefaultId(MAX_RANDOM_CHAR_LENGTH);
    }
}
