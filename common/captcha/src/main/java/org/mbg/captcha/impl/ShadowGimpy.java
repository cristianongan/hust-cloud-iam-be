package org.mbg.captcha.impl;

import com.jhlabs.image.RippleFilter;
import com.jhlabs.image.ShadowFilter;
import com.jhlabs.image.TransformFilter;
import org.mbg.captcha.GimpyEngine;
import org.mbg.captcha.NoiseProducer;
import org.mbg.captcha.util.Configurable;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * {@link ShadowGimpy} applies image distortion by adding a shadow effect on text and two layers of noise.
 * <p>
 * This class extends {@link Configurable}, utilizing configuration capabilities
 * from the {@link Configurable} class. It implements the
 * {@link GimpyEngine} interface to provide a specific distortion mechanism.
 * <p>
 * The class uses a shadow filter and ripple filter to create visual effects
 * and noise producers for adding additional distortion elements to the image.
 * <p>
 * This implementation is part of the Captcha library and is used for CAPTCHA text transformation.
 * <p>
 * Implements a combination of shadow effects, wave ripples, and noise layers to achieve CAPTCHA distortion.
 *
 * @author LinhLH
 * @since 19/04/2025
 */
public class ShadowGimpy extends Configurable implements GimpyEngine {
	/**
	 * Applies distortion to the given image by adding a shadow effect, ripple effect,
	 * and multiple noise layers. This method is part of the CAPTCHA image processing pipeline
	 * to obfuscate text for security purposes.
	 * <p>
	 * The method integrates a combination of shadow filtering, ripple transformations,
	 * and noise production to create a visually distorted image, making it difficult
	 * for automated systems to interpret.
	 *
	 * @param baseImage the original image that needs distortion
	 * @return the distorted image with applied effects
	 */
	public BufferedImage getDistortedImage(BufferedImage baseImage) {
        NoiseProducer noiseProducer = getConfig().getNoiseImpl();
        BufferedImage distortedImage = new BufferedImage(baseImage.getWidth(),
                baseImage.getHeight(), BufferedImage.TYPE_INT_ARGB);

        Graphics2D graph = (Graphics2D) distortedImage.getGraphics();

        ShadowFilter shadowFilter = new ShadowFilter();
        shadowFilter.setRadius(10);
        shadowFilter.setDistance(5);
        shadowFilter.setOpacity(1);

        Random rand = new Random();

        RippleFilter rippleFilter = new RippleFilter();
        rippleFilter.setWaveType(RippleFilter.SINE);
        rippleFilter.setXAmplitude(7.6f);
        rippleFilter.setYAmplitude(rand.nextFloat() + 1.0f);
        rippleFilter.setXWavelength(rand.nextInt(7) + 8);
        rippleFilter.setYWavelength(rand.nextInt(3) + 2);
        rippleFilter.setEdgeAction(TransformFilter.BILINEAR);

        BufferedImage effectImage = rippleFilter.filter(baseImage, null);
        effectImage = shadowFilter.filter(effectImage, null);

        graph.drawImage(effectImage, 0, 0, null, null);
        graph.dispose();

        // draw lines over the image and/or text
        noiseProducer.makeNoise(distortedImage, .1f, .1f, .25f, .25f);
        noiseProducer.makeNoise(distortedImage, .1f, .25f, .5f, .9f);

        return distortedImage;
    }
}
