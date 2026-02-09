package com.hust.captcha;

import java.awt.image.BufferedImage;

/**
 * The {@code GimpyEngine} interface defines a contract for applying distortions
 * to images, typically for security purposes such as CAPTCHA generation.
 * <p>
 * Implementations of this interface are responsible for manipulating images with
 * effects such as ripples, shadows, noise, or other distortions in order to obfuscate
 * visual content, making it challenging for automated systems to interpret.
 * <p>
 * This interface is part of image manipulation utilities and is often used in conjunction
 * with other tools for generating secure and robust CAPTCHA systems.
 * <p>
 * Implementations should provide specific distortion algorithms via the
 * {@link #getDistortedImage(BufferedImage)} method.
 *
 * @author LinhLH
 * @since 19/04/2025
 */
public interface GimpyEngine {
	/**
	 * Applies distortion effects to the given base image, typically used for obfuscating
	 * the image content in CAPTCHA systems.
	 * <p>
	 * This method processes the provided {@link BufferedImage} with distortion algorithms
	 * to make it more challenging for automated systems to interpret.
	 *
	 * @param baseImage The input {@link BufferedImage} to which distortion effects will be applied.
	 * @return A new {@link BufferedImage} instance containing the distorted image.
	 */
	BufferedImage getDistortedImage(BufferedImage baseImage);
}