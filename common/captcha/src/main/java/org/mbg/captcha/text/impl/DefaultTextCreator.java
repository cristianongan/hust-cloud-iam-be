package org.mbg.captcha.text.impl;

import org.mbg.captcha.text.TextProducer;
import org.mbg.captcha.util.Configurable;

import java.security.SecureRandom;

/**
 * {@code DefaultTextCreator} is responsible for generating random text.
 * <p>
 * It extends {@code Configurable} to utilize configuration properties
 * and implements the {@code TextProducer} interface, providing a concrete implementation
 * for text generation. The generated text is based on the configuration's specified
 * character set and length.
 * <p>
 * This implementation is generally used in CAPTCHA systems or other contexts
 * where random text generation is required.
 *
 * @author LinhLH
 * @since 19/04/2025
 */
public class DefaultTextCreator extends Configurable implements TextProducer {
    /**
     * @return the random text
     */
    public String getText() {
        int length = getConfig().getTextProducerCharLength();

        char[] chars = getConfig().getTextProducerCharString();

		SecureRandom rand = new SecureRandom();

        StringBuilder text = new StringBuilder();

        for (int i = 0; i < length; i++) {
            text.append(chars[rand.nextInt(chars.length)]);
        }

        return text.toString();
    }
}
