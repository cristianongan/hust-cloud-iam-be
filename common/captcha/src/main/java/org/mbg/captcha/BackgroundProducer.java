package org.mbg.captcha;

import org.mbg.captcha.impl.DefaultBackground;

import java.awt.image.BufferedImage;

/**
 * {@link BackgroundProducer} is responsible for generating a background
 * and applying it to a given image.
 *
 * <p>
 * Implementations of this interface define the method to add a custom background
 * to an image, enhancing its visual appeal or meeting specific design requirements.
 *
 * <p>
 * The {@code addBackground} method takes a source image and produces
 * a new image with the background applied.
 *
 * @see DefaultBackground
 * @see Constants#CAPTCHA_BACKGROUND_IMPL
 * @see Constants#CAPTCHA_BACKGROUND_CLR_FROM
 * @see Constants#CAPTCHA_BACKGROUND_CLR_TO
 *
 * @author LinhLH
 * @since 19/04/2025
 */
public interface BackgroundProducer {
    BufferedImage addBackground(BufferedImage image);
}
