package org.mbg.captcha.text;

import java.awt.image.BufferedImage;

/**
 * {@code WordRenderer} is an interface defining a blueprint for rendering words onto images.
 * <p>
 * Implementations of this interface are expected to generate graphical representations of
 * given words by rendering them onto {@link BufferedImage} objects with specified dimensions.
 * <p>
 * The primary use of this interface is within CAPTCHA systems, where dynamically rendering
 * text onto an image is a critical component of creating visually secure challenges.
 * <p>
 * @author LinhLH
 * @since 19/04/2025
 */
public interface WordRenderer {
    BufferedImage renderWord(String word, int width, int height);
}
