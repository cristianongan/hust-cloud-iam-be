package org.mbg.captcha.impl;

import org.mbg.captcha.NoiseProducer;
import org.mbg.captcha.util.Configurable;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * The {@code DefaultNoise} class is the default implementation of {@link NoiseProducer}.
 * It is responsible for adding noise to an image by drawing a series of lines based on cubic curves.
 *
 * <p>
 * The noise effect is achieved by generating a set of random points on a cubic curve
 * and rendering it as a sequence of lines with varying stroke thicknesses and directions.
 * The color of the noise is configurable.
 *
 * <p>
 * This class extends {@link Configurable}, allowing it to use configuration properties for noise characteristics.
 *
 * @author LinhLH
 * @since 19/04/2025
 */
public class DefaultNoise extends Configurable implements NoiseProducer {
    /**
	 * The default implementation of {@link NoiseProducer}, adds a noise on an
	 * image.
	 *
	 * <p>
	 * This implementation generates noise by drawing a series of lines with random
	 * curves and characteristics on the image. The points of the curves are
	 * determined based on the input factors.
	 *
	 * <p>
	 * Noise is applied using cubic curves that are modified and rendered as lines
	 * on the image. These lines vary in thickness and direction to create a noise
	 * effect visually.
	 *
	 * <p>
	 * The color of the noise is retrieved from the configuration.
	 *
	 * @author LinhLH
	 * @since October 2023
	 */
    public void makeNoise(BufferedImage image, float factorOne,
                          float factorTwo, float factorThree, float factorFour) {
        Color color = getConfig().getNoiseColor();

        // image size
        int width = image.getWidth();
        int height = image.getHeight();

        // the points where the line changes the stroke and direction
        Point2D[] pts = null;
        Random rand = new Random();

        // the curve from where the points are taken
        CubicCurve2D cc = new CubicCurve2D.Float(width * factorOne, height
                * rand.nextFloat(), width * factorTwo, height
                * rand.nextFloat(), width * factorThree, height
                * rand.nextFloat(), width * factorFour, height
                * rand.nextFloat());

        // creates an iterator to define the boundary of the flattened curve
        PathIterator pi = cc.getPathIterator(null, 2);
        Point2D tmp[] = new Point2D[200];
        int i = 0;

        // while pi is iterating the curve, adds points to tmp array
        while (!pi.isDone()) {
            float[] coords = new float[6];
            switch (pi.currentSegment(coords)) {
                case PathIterator.SEG_MOVETO:
                case PathIterator.SEG_LINETO:
                    tmp[i] = new Point2D.Float(coords[0], coords[1]);
            }
            i++;
            pi.next();
        }

        pts = new Point2D[i];
        System.arraycopy(tmp, 0, pts, 0, i);

        Graphics2D graph = (Graphics2D) image.getGraphics();
        graph.setRenderingHints(new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON));

        graph.setColor(color);

        // for the maximum 3 point change the stroke and direction
        for (i = 0; i < pts.length - 1; i++) {
            if (i < 3)
                graph.setStroke(new BasicStroke(0.9f * (4 - i)));
            graph.drawLine((int) pts[i].getX(), (int) pts[i].getY(),
                    (int) pts[i + 1].getX(), (int) pts[i + 1].getY());
        }

        graph.dispose();
    }
}
