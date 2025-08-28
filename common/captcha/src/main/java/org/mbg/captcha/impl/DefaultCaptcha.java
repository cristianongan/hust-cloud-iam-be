package org.mbg.captcha.impl;

import org.mbg.captcha.BackgroundProducer;
import org.mbg.captcha.GimpyEngine;
import org.mbg.captcha.Producer;
import org.mbg.captcha.text.WordRenderer;
import org.mbg.captcha.util.Configurable;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;

/**
 * The {@code DefaultCaptcha} class represents the default CAPTCHA implementation
 * responsible for generating CAPTCHA images with distorted text and backgrounds.
 * It extends the {@code Configurable} class and implements the {@code Producer} interface.
 *
 * <p>
 * It utilizes various configurations to manage the appearance of the CAPTCHA,
 * such as word rendering, image distortion, adding background, and optionally drawing a border.
 *
 * <p>
 * This class provides methods to create CAPTCHA images and generate the textual content
 * for such images.
 *
 * @author LinhLH
 * @since 19/04/2025
 */
public class DefaultCaptcha extends Configurable implements Producer {
    private int width = 200;

    private int height = 50;

	/**
	 * Generates a CAPTCHA image by rendering a given text, applying distortion,
	 * adding a background, and optionally drawing a border around the image.
	 * <p>
	 * This method uses the configurations defined in the {@code Configurable} class
	 * for rendering text, applying distortions, adding backgrounds, and drawing borders.
	 * Adjustments such as image size and border drawing behavior are derived from these configurations.
	 *
	 * @param text The text string to be rendered and displayed on the CAPTCHA image.
	 * @return A {@link BufferedImage} object representing the generated CAPTCHA image,
	 * which includes rendered text, distortions, and background effects.
	 */
	public BufferedImage createImage(String text) {
        WordRenderer wordRenderer = getConfig().getWordRendererImpl();
        GimpyEngine gimpyEngine = getConfig().getObfuscateImpl();
        BackgroundProducer backgroundProducer = getConfig().getBackgroundImpl();
        boolean isBorderDrawn = getConfig().isBorderDrawn();
        this.width = getConfig().getWidth();
        this.height = getConfig().getHeight();

        BufferedImage bi = wordRenderer.renderWord(text, width, height);
        bi = gimpyEngine.getDistortedImage(bi);
        bi = backgroundProducer.addBackground(bi);
        Graphics2D graphics = bi.createGraphics();
        if (isBorderDrawn) {
            drawBox(graphics);
        }
        return bi;
    }

	/**
	 * Represents the logic for drawing a rectangular box on a given graphical context.
	 * The method uses the configuration to determine border color and thickness while
	 * rendering the box edges.
	 * <p>
	 * This private utility is focused on rendering edges around the CAPTCHA image.
	 *
	 * @param graphics The graphics context ({@link Graphics2D}) used to draw the box.
	 *
	 * @author LinhLH
	 * @since 19/04/2025
	 */
    private void drawBox(Graphics2D graphics) {
        Color borderColor = getConfig().getBorderColor();
        int borderThickness = getConfig().getBorderThickness();

        graphics.setColor(borderColor);

        if (borderThickness != 1) {
            BasicStroke stroke = new BasicStroke((float) borderThickness);
            graphics.setStroke(stroke);
        }

        Line2D line1 = new Line2D.Double(0, 0, 0, height);
        graphics.draw(line1);
        Line2D line2 = new Line2D.Double(0, 0, width, 0);
        graphics.draw(line2);
        line2 = new Line2D.Double(0, height - 1, width, height - 1);
        graphics.draw(line2);
        line2 = new Line2D.Double(width - 1, height - 1, width - 1, 0);
        graphics.draw(line2);
    }

    /**
     * @return the text to be drawn
     */
    public String createText() {
        return getConfig().getTextProducerImpl().getText();
    }
}
