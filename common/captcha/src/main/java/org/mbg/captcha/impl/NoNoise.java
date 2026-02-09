package com.hust.captcha.impl;

import com.hust.captcha.NoiseProducer;
import com.hust.captcha.util.Configurable;

import java.awt.image.BufferedImage;

/**
 * {@link NoNoise} is a {@link NoiseProducer} implementation that does not add noise to an image.
 * It provides a no-operation (no-op) implementation of the {@code makeNoise} method.
 *
 * <p>
 * This class extends {@link Configurable}, allowing it to integrate with configuration management.
 *
 * <p>
 * This implementation is useful when no noise is desired in an image processing pipeline.
 *
 * <p>
 * Implements the {@link NoiseProducer} interface from the Kaptcha library.
 *
 * <p>
 * Note: The {@code makeNoise} method in this class does not perform any operations.
 *
 * @author LinhLH
 * @since 19/04/2025
 */
public class NoNoise extends Configurable implements NoiseProducer {
    /**
     *
     */
    public void makeNoise(BufferedImage image, float factorOne,
                          float factorTwo, float factorThree, float factorFour) {
        //Do nothing.
    }
}
