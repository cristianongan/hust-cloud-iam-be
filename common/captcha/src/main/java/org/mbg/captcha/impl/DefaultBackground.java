package org.mbg.captcha.impl;

import org.mbg.captcha.BackgroundProducer;
import org.mbg.captcha.util.Configurable;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 * {@link DefaultBackground} is responsible for adding a gradient background
 * to a given image.
 * <p>
 * This class extends {@link Configurable} to utilize configurable
 * properties for background color gradients. It implements
 * {@link BackgroundProducer} interface for background generation.
 *
 * <p>
 * The {@link DefaultBackground} class retrieves the gradient color range
 * from the configuration and applies it to the specified base image,
 * creating a smooth background effect.
 *
 * <p>
 * Rendering settings such as antialiasing and color rendering quality
 * are configured to enhance the output image quality.
 *
 * <p>
 * This class ensures that the base image and the gradient blend seamlessly
 * to produce the final output.
 *
 * @author LinhLH
 * @since 19/04/2025
 */
public class DefaultBackground extends Configurable implements BackgroundProducer {
	/**
	 * Adds a gradient background to the provided base image, creating a seamless
	 * blend between the original image and the specified background colors.
	 * <p>
	 * The gradient is determined using the configurable background colors
	 * retrieved from the implementation's configuration.
	 *
	 * @param baseImage the original image to which the background gradient will be added
	 * @return a new {@link BufferedImage} instance combining the gradient background
	 *         with the original image
	 */
	public BufferedImage addBackground(BufferedImage baseImage) {
        Color colorFrom = getConfig().getBackgroundColorFrom();
        Color colorTo = getConfig().getBackgroundColorTo();

        int width = baseImage.getWidth();
        int height = baseImage.getHeight();

        // create an opaque image
        BufferedImage imageWithBackground = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);

        Graphics2D graph = (Graphics2D) imageWithBackground.getGraphics();
        RenderingHints hints = new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_OFF);

        hints.add(new RenderingHints(RenderingHints.KEY_COLOR_RENDERING,
                RenderingHints.VALUE_COLOR_RENDER_QUALITY));
        hints.add(new RenderingHints(RenderingHints.KEY_ALPHA_INTERPOLATION,
                RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY));

        hints.add(new RenderingHints(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY));

        graph.setRenderingHints(hints);

        GradientPaint paint = new GradientPaint(0, 0, colorFrom, width, height,
                colorTo);
        graph.setPaint(paint);
        graph.fill(new Rectangle2D.Double(0, 0, width, height));

        // draw the transparent image over the background
        graph.drawImage(baseImage, 0, 0, null);

        return imageWithBackground;
    }
}
