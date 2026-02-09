package com.hust.captcha;

import java.awt.image.BufferedImage;

/**
 * The {@code NoiseProducer} interface defines a contract for adding noise
 * to images, typically for obfuscating image content in applications such as CAPTCHA generation.
 *
 * <p>
 * Noise is applied to an image based on the input factors provided to the {@code makeNoise} method.
 * This interface is a key part of the image manipulation pipeline, allowing for flexible
 * and customizable noise generation strategies.
 *
 * <p>
 * Implementations of this interface can define the type and characteristics of the noise,
 * including shape, color, and intensity. By introducing noise to an image, the interface
 * enhances security by making it challenging for automated systems to interpret the content
 * of the image.
 *
 * <p>
 * Extensions of this interface can include complex algorithms or simple noise patterns,
 * offering developers a wide range of customization possibilities for their noise generation needs.
 *
 * @author LinhLH
 * @since 19/04/2025
 */
public interface NoiseProducer {
	/**
	 * Applies noise to the given image with customizable intensity defined by four factors.
	 * <p>
	 * This method is typically used in the context of CAPTCHA generation or other image
	 * obfuscation processes to make the image more challenging for automated systems
	 * to interpret or analyze.
	 *
	 * @param image      the {@link BufferedImage} on which noise will be applied.
	 * @param factorOne  a float value representing the first noise intensity factor.
	 * @param factorTwo  a float value representing the second noise intensity factor.
	 * @param factorThree a float value representing the third noise intensity factor.
	 * @param factorFour  a float value representing the fourth noise intensity factor.
	 */
    void makeNoise(BufferedImage image, float factorOne, float factorTwo, float factorThree, float factorFour);
}