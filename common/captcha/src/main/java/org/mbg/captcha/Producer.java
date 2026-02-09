package com.hust.captcha;

import com.hust.captcha.impl.NoNoise;

import java.awt.image.BufferedImage;

/**
 * The {@code Producer} interface represents a contract for generating textual content
 * and producing images containing distorted or styled text, often used in CAPTCHA systems.
 *
 * <p>
 * This interface defines methods for creating textual content and rendering such text
 * onto an image, typically used in securing web forms or authenticating users.
 *
 * <p>
 * Implementations are expected to use various customization properties, including
 * text distortion, customization of fonts and colors, and optional graphical effects,
 * to fulfill the requirements of CAPTCHA generation and other text rendering systems.
 *
 * <p>
 * It serves as a key component in generating secure and reliable CAPTCHA solutions,
 * offering extensibility and flexibility for developers to implement their
 * desired behavior for image and text creation.
 *
 * @see Constants
 * @see BackgroundProducer
 * @see NoiseProducer
 * @see GimpyEngine
 * @see NoNoise
 *
 * @author LinhLH
 * @since 19/04/2025
 */
public interface Producer {
    /**
     * Generates an image containing the specified text, typically used for rendering
     * CAPTCHA-style obfuscated or styled text onto an image.
     *
     * <p>
     * The method creates an instance of {@link BufferedImage} and uses the given
     * text to draw styled or distorted characters according to the implementation
     * specifics. The resulting image can be further processed or used for user
     * authentication purposes.
     *
     * @param text The text that will be rendered onto the image.
     * @return A {@link BufferedImage} instance containing the rendered text.
     */
    BufferedImage createImage(String text);

    /**
     * Generates a random or predefined textual string as per the implementation.
     * <p>
     * This method is typically used in CAPTCHA systems to create a text value
     * that can be rendered and displayed to users for verification purposes.
     * Implementations can define custom generation logic, including random
     * character sequences or predefined patterns.
     *
     * @return A {@code String} representing the generated text.
     */
    String createText();
}