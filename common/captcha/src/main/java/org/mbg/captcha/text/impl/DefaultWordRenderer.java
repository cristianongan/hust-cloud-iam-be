package org.mbg.captcha.text.impl;

import org.mbg.captcha.text.WordRenderer;
import org.mbg.captcha.util.Configurable;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * {@code DefaultWordRenderer} is responsible for rendering words into an image.
 * <p>
 * This class extends {@code Configurable}, allowing it to utilize configuration properties
 * such as font size, font types, font color, and character spacing to customize the word rendering process.
 * <p>
 * It implements the {@code WordRenderer} interface, providing functional logic to create
 * a {@code BufferedImage} representation of a word with specified dimensions.
 * <p>
 * This implementation is primarily used in generating CAPTCHA images or any other contexts
 * requiring dynamic word rendering.
 *
 * @author LinhLH
 * @since 19/04/2025
 */
public class DefaultWordRenderer extends Configurable implements WordRenderer {
	/**
	 * Renders a word onto a {@link BufferedImage} with the specified dimensions.
	 * <p>
	 * The rendering process considers configurable properties such as font size, font styles,
	 * font color, and character spacing. The word is centered horizontally within the image
	 * while maintaining a visually appealing design through antialiasing and rendering quality hints.
	 *
	 * @param word the word to render
	 * @param width the width of the output {@link BufferedImage}
	 * @param height the height of the output {@link BufferedImage}
	 * @return a {@link BufferedImage} containing the rendered word
	 */
	public BufferedImage renderWord(String word, int width, int height) {
        int fontSize = getConfig().getTextProducerFontSize();
        Font[] fonts = getConfig().getTextProducerFonts(fontSize);
        Color color = getConfig().getTextProducerFontColor();
        int charSpace = getConfig().getTextProducerCharSpace();
        BufferedImage image = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2D = image.createGraphics();
        g2D.setColor(color);

        RenderingHints hints = new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        hints.add(new RenderingHints(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY));
        g2D.setRenderingHints(hints);

        FontRenderContext frc = g2D.getFontRenderContext();
        Random random = new Random();

        int startPosY = (height - fontSize) / 5 + fontSize;

        char[] wordChars = word.toCharArray();
        Font[] chosenFonts = new Font[wordChars.length];
        int[] charWidths = new int[wordChars.length];
        int widthNeeded = 0;
        for (int i = 0; i < wordChars.length; i++) {
            chosenFonts[i] = fonts[random.nextInt(fonts.length)];

            char[] charToDraw = new char[]{
                    wordChars[i]
            };
            GlyphVector gv = chosenFonts[i].createGlyphVector(frc, charToDraw);
            charWidths[i] = (int) gv.getVisualBounds().getWidth();
            if (i > 0) {
                widthNeeded = widthNeeded + 2;
            }
            widthNeeded = widthNeeded + charWidths[i];
        }

        int startPosX = (width - widthNeeded) / 2;
        for (int i = 0; i < wordChars.length; i++) {
            g2D.setFont(chosenFonts[i]);
            char[] charToDraw = new char[]{
                    wordChars[i]
            };
            g2D.drawChars(charToDraw, 0, charToDraw.length, startPosX, startPosY);
            startPosX = startPosX + charWidths[i] + charSpace;
        }

        return image;
    }
}
