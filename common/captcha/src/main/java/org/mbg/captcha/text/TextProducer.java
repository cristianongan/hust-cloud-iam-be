package com.hust.captcha.text;


/**
 * {@code TextProducer} is an interface representing a blueprint for generating text.
 * <p>
 * Implementing classes are expected to provide their own logic for text generation,
 * which can be utilized for various purposes such as CAPTCHA systems.
 *
 * <p>
 * Examples of implementations include generating random text, producing Chinese characters,
 * or selecting predefined first names.
 *
 * <p>
 * This interface ensures a standardized method signature for generating text,
 * facilitating consistency across different text producer implementations.
 *
 * <p>
 * @author LinhLH
 * @since 19/04/2025
 */
public interface TextProducer {
    String getText();
}