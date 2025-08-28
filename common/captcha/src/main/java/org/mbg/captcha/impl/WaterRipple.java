package org.mbg.captcha.impl;

import com.jhlabs.image.RippleFilter;
import com.jhlabs.image.TransformFilter;
import com.jhlabs.image.WaterFilter;
import org.mbg.captcha.GimpyEngine;
import org.mbg.captcha.NoiseProducer;
import org.mbg.captcha.util.Configurable;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * {@link WaterRipple} applies a water ripple effect to an image along with layered noise distortion.
 * <p>
 * This class extends {@link Configurable}, allowing it to utilize configuration settings provided
 * by the {@link Configurable} class. It implements the {@link GimpyEngine} interface, providing
 * a specific implementation of the method for applying visual distortions to images.
 * <p>
 * Features of this implementation include the integration of water and ripple filters for creating
 * unique distortion effects combined with layered noise patterns for enhanced obfuscation.
 * <p>
 * This implementation is typically part of CAPTCHA generation systems where it is used
 * to distort images and text for security purposes, making it challenging for automated
 * recognition systems to interpret them correctly.
 * <p>
 * Note: The distortion combines multiple image effects such as sine wave ripples,
 * water patterns, and configurable noise configurations.
 *
 * @author LinhLH
 * @since 19/04/2025
 */
public class WaterRipple extends Configurable implements GimpyEngine {
	/**
	 * Applies a combination of ripple, water distortion filters, and layered noise to the given image,
	 * creating a distorted effect for security purposes such as CAPTCHA generation.
	 *
	 * @param baseImage the original {@link BufferedImage} to which distortions will be applied.
	 * @return a new {@link BufferedImage} with the applied distortion effects.
	 */
	public BufferedImage getDistortedImage(BufferedImage baseImage) {
        NoiseProducer noiseProducer = getConfig().getNoiseImpl();
        BufferedImage distortedImage = new BufferedImage(baseImage.getWidth(),
                baseImage.getHeight(), BufferedImage.TYPE_INT_ARGB);

        Graphics2D graphics = (Graphics2D) distortedImage.getGraphics();

        RippleFilter rippleFilter = new RippleFilter();
        rippleFilter.setWaveType(RippleFilter.SINE);
        rippleFilter.setXAmplitude(2.6f);
        rippleFilter.setYAmplitude(1.7f);
        rippleFilter.setXWavelength(15);
        rippleFilter.setYWavelength(5);
        rippleFilter.setEdgeAction(TransformFilter.NEAREST_NEIGHBOUR);

        WaterFilter waterFilter = new WaterFilter();
        waterFilter.setAmplitude(1.5f);
        waterFilter.setPhase(10);
        waterFilter.setWavelength(2);

        BufferedImage effectImage = waterFilter.filter(baseImage, null);
        effectImage = rippleFilter.filter(effectImage, null);

        graphics.drawImage(effectImage, 0, 0, null, null);

        graphics.dispose();

        noiseProducer.makeNoise(distortedImage, .1f, .1f, .25f, .25f);
        noiseProducer.makeNoise(distortedImage, .1f, .25f, .5f, .9f);
        return distortedImage;
    }
}
