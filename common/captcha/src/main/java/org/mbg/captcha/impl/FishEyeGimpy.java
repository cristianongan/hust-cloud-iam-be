package org.mbg.captcha.impl;

import org.mbg.captcha.GimpyEngine;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * {@link FishEyeGimpy} applies a fish-eye effect distortion to the image by
 * creating horizontal and vertical lines, and a pixel manipulation to achieve
 * the stated effect.
 * <p>
 * {@link FishEyeGimpy} implements {@link GimpyEngine}.
 * <p>
 * This implementation intentionally distorts the image to make it more
 * challenging for automated systems to parse.
 * <p>
 * It features random line spacing, coloration, and a fish-eye type distortion
 * based on a mathematical formula.
 *
 * @author LinhLH
 * @since 19/04/2025
 */
public class FishEyeGimpy implements GimpyEngine {
    /**
     * Applies distortion by adding fish eye effect and horizontal vertical
     * lines.
     *
     * @param baseImage the base image
     * @return the distorted image
     */
    public BufferedImage getDistortedImage(BufferedImage baseImage) {

        Graphics2D graph = (Graphics2D) baseImage.getGraphics();
        int imageHeight = baseImage.getHeight();
        int imageWidth = baseImage.getWidth();

        // want lines put them in a variable so we might configure these later
        int horizontalLines = imageHeight / 7;
        int verticalLines = imageWidth / 7;

        // calculate space between lines
        int horizontalGaps = imageHeight / (horizontalLines + 1);
        int verticalGaps = imageWidth / (verticalLines + 1);

        // draw the horizontal stripes
        for (int i = horizontalGaps; i < imageHeight; i = i + horizontalGaps) {
            graph.setColor(Color.blue);
            graph.drawLine(0, i, imageWidth, i);

        }

        // draw the vertical stripes
        for (int i = verticalGaps; i < imageWidth; i = i + verticalGaps) {
            graph.setColor(Color.red);
            graph.drawLine(i, 0, i, imageHeight);

        }

        // create a pixel array of the original image.
        // we need this later to do the operations on.
        int[] pix = new int[imageHeight * imageWidth];
        int j = 0;

        for (int j1 = 0; j1 < imageWidth; j1++) {
            for (int k1 = 0; k1 < imageHeight; k1++) {
                pix[j] = baseImage.getRGB(j1, k1);
                j++;
            }

        }

        double distance = ranInt(imageWidth / 4, imageWidth / 3);

        // put the distortion in the (dead) middle
        int widthMiddle = baseImage.getWidth() / 2;
        int heightMiddle = baseImage.getHeight() / 2;

        // again iterate over all pixels.
        for (int x = 0; x < baseImage.getWidth(); x++) {
            for (int y = 0; y < baseImage.getHeight(); y++) {

                int relX = x - widthMiddle;
                int relY = y - heightMiddle;

                double d1 = Math.sqrt(relX * relX + relY * relY);
                if (d1 < distance) {

                    int j2 = widthMiddle
                            + (int) (((fishEyeFormula(d1 / distance) * distance) / d1) * (double) (x - widthMiddle));
                    int k2 = heightMiddle
                            + (int) (((fishEyeFormula(d1 / distance) * distance) / d1) * (double) (y - heightMiddle));
                    baseImage.setRGB(x, y, pix[j2 * imageHeight + k2]);
                }
            }

        }

        return baseImage;
    }

	/**
	 * Class {@code FishEyeGimpy} applies image distortions using a fish-eye
	 * effect and performs additional manipulations such as adding random integer
	 * functionality for distortion variability.
	 *
	 * <p>
	 * This is a part of image manipulation utilities for CAPTCHA generation.
	 *
	 * <p>
	 *
	 * @author LinhLH
	 * @since 19/04/2025
	 */
	private int ranInt(int i, int j) {
        double d = Math.random();
        return (int) ((double) i + (double) ((j - i) + 1) * d);
    }

	/**
	 * Calculates a fish-eye distortion for a given input. The input value will be altered
	 * using a distortion formula that generates a fish-eye-like effect within the range of [0, 1].
	 * Values outside this range will return either 0.0 or the input value.
	 *
	 * <p>
	 *
	 * @param s the input value for the distortion formula; should ideally be in the range [0, 1]
	 * @return the distorted value after applying the fish-eye effect
	 */
	private double fishEyeFormula(double s) {
        if (s < 0.0D)
            return 0.0D;
        if (s > 1.0D)
            return s;
        else
            return -0.75D * s * s * s + 1.5D * s * s + 0.25D * s;
    }
}
