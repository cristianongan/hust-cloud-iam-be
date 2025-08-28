package org.mbg.captcha.util;

import org.mbg.captcha.BackgroundProducer;
import org.mbg.captcha.Constants;
import org.mbg.captcha.GimpyEngine;
import org.mbg.captcha.NoiseProducer;
import org.mbg.captcha.Producer;
import org.mbg.captcha.impl.DefaultBackground;
import org.mbg.captcha.impl.DefaultCaptcha;
import org.mbg.captcha.impl.DefaultNoise;
import org.mbg.captcha.impl.WaterRipple;
import org.mbg.captcha.text.TextProducer;
import org.mbg.captcha.text.WordRenderer;
import org.mbg.captcha.text.impl.DefaultTextCreator;
import org.mbg.captcha.text.impl.DefaultWordRenderer;
import lombok.Getter;

import java.awt.*;
import java.util.Properties;

/**
 * Represents the configuration settings and utility methods for a CAPTCHA system.
 * The class provides access to various properties related to customization and
 * behavior of the CAPTCHA, including visual appearance, text generation, noise,
 * background, dimensions, and session management.
 * <p>
 * This class encapsulates a set of `Properties` and uses a helper object to handle
 * property retrieval, validation, and transformation into the appropriate data types
 * or default values.
 * <p>
 * This class is designed to be highly configurable by changing the property
 * definitions with appropriate values.
 *
 * @author LinhLH
 * @since 19/04/2025
 */
public class Config {
	/**
	 * Represents configuration properties that define the behavior and attributes
	 * associated with the configuration of the system.
	 * <p>
	 * This field contains the key-value pairs used for various configuration aspects.
	 * It is immutable and is initialized during the construction of the {@link Config} class.
	 * <p>
	 * @since 19/04/2025
	 */
	@Getter
	private final Properties properties;

	/**
	 * Represents a helper for configuration parsing and handling within the system.
	 * <p>
	 * This variable is used to encapsulate an instance of {@link ConfigHelper},
	 * providing methods to handle various configuration-related operations such as
	 * parsing colors, fonts, boolean values, and more.
	 *
	 * @since 19/04/2025
	 */
	private final ConfigHelper helper;

	/**
	 * Provides configuration settings for the application.
	 * <p>
	 * This class holds properties and manages configuration settings using the {@code ConfigHelper}.
	 * <p>
	 * Author: LinhLH
	 * <p>
	 * Since: 19/04/2025
	 */
	public Config(Properties properties) {
        this.properties = properties;
        this.helper = new ConfigHelper();
    }

	/**
	 * Determines whether the border is drawn based on the configuration settings.
	 * <p>
	 * This method retrieves the "captcha.border" property from the configuration and evaluates
	 * whether the border should be drawn, using a default value of {@code true}
	 * if the property is not specified or invalid.
	 *
	 * @return {@code true} if the border is drawn; {@code false} otherwise.
	 */
	public boolean isBorderDrawn() {
        String paramName = Constants.CAPTCHA_BORDER;
        String paramValue = this.properties.getProperty(paramName);
        return this.helper.getBoolean(paramName, paramValue, true);
    }

	/**
	 * Retrieves the border color for the CAPTCHA configuration.
	 * <p>
	 * This method fetches the "captcha.border.color" property from the configuration
	 * and converts it into a {@link Color} object using the {@code getColor} helper method.
	 * If the property is not specified or invalid, it defaults to {@link Color#BLACK}.
	 *
	 * @return The {@link Color} object representing the border color; defaults to {@link Color#BLACK}.
	 */
	public Color getBorderColor() {
        String paramName = Constants.CAPTCHA_BORDER_COLOR;
        String paramValue = this.properties.getProperty(paramName);
        return this.helper.getColor(paramName, paramValue, Color.BLACK);
    }

	/**
	 * Retrieves the border thickness for the CAPTCHA configuration.
	 * <p>
	 * This method fetches the "captcha.border.thickness" property from the configuration
	 * and ensures it's a positive integer using the {@code getPositiveInt} helper method.
	 * If the property is not specified or invalid, it defaults to {@code 1}.
	 *
	 * @return The thickness of the border as a positive integer; defaults to {@code 1}.
	 *
	 * @author LinhLH
	 * @since 19/04/2025
	 */
	public int getBorderThickness() {
        String paramName = Constants.CAPTCHA_BORDER_THICKNESS;
        String paramValue = this.properties.getProperty(paramName);
        return this.helper.getPositiveInt(paramName, paramValue, 1);
    }

	/**
	 * Retrieves the implementation of the {@link Producer} configured for CAPTCHA generation.
	 * <p>
	 * This method fetches the "captcha.producer.impl" property from the configuration
	 * to dynamically determine the specific {@link Producer} implementation. If no custom
	 * implementation is specified or an error occurs during instantiation, a {@link DefaultCaptcha}
	 * instance is returned as the default.
	 *
	 * @return The {@link Producer} instance responsible for generating CAPTCHA images and text.
	 *
	 * @author LinhLH
	 * @since 19/04/2025
	 */
	public Producer getProducerImpl() {
        String paramName = Constants.CAPTCHA_PRODUCER_IMPL;

        String paramValue = this.properties.getProperty(paramName);

        return (Producer) this.helper.getClassInstance(paramName, paramValue, new DefaultCaptcha(), this);
    }

	/**
	 * Retrieves the implementation of the {@link TextProducer} configured for CAPTCHA text generation.
	 * <p>
	 * This method fetches the "captcha.text-producer.impl" property from the configuration
	 * to dynamically determine the specific {@link TextProducer} implementation. If no custom
	 * implementation is specified or an error occurs during instantiation, a {@link DefaultTextCreator}
	 * instance is returned as the default.
	 *
	 * @return The {@link TextProducer} instance responsible for generating CAPTCHA text.
	 */
	public TextProducer getTextProducerImpl() {
        String paramName = Constants.CAPTCHA_TEXT_PRODUCER_IMPL;
        String paramValue = this.properties.getProperty(paramName);

        return (TextProducer) this.helper.getClassInstance(paramName, paramValue,
                new DefaultTextCreator(), this);
    }

	/**
	 * Retrieves the characters used for CAPTCHA text generation.
	 * <p>
	 * This method fetches the configuration property defined by
	 * {@link Constants#CAPTCHA_TEXT_PRODUCER_CHAR_STRING} and converts its value
	 * into a character array. If the property is not specified or invalid,
	 * it defaults to the character set {@code "abcde2345678gfynmnpwx"}.
	 *
	 * @return A {@code char[]} containing the characters for CAPTCHA text generation.
	 *         Defaults to {@code "abcde2345678gfynmnpwx"} if no valid configuration is found.
	 */
	public char[] getTextProducerCharString() {
        String paramName = Constants.CAPTCHA_TEXT_PRODUCER_CHAR_STRING;

        String paramValue = this.properties.getProperty(paramName);

        return this.helper.getChars(paramValue, "abcde2345678gfynmnpwx".toCharArray());
    }

	/**
	 * Retrieves the character length for the CAPTCHA text generation.
	 * <p>
	 * This method fetches the {@code "captcha.text-producer.char.length"} configuration property,
	 * ensures the value is a positive integer using the {@code getPositiveInt} helper method,
	 * and defaults to {@code 5} if the property is not specified or invalid.
	 *
	 * @return The character length for the CAPTCHA text as a positive integer; defaults to {@code 5}.
	 */
	public int getTextProducerCharLength() {
        String paramName = Constants.CAPTCHA_TEXT_PRODUCER_CHAR_LENGTH;

        String paramValue = this.properties.getProperty(paramName);

        return this.helper.getPositiveInt(paramName, paramValue, 5);
    }

	/**
	 * Retrieves the list of fonts used for the CAPTCHA text generation based on the configuration.
	 * <p>
	 * This method fetches the font names defined by
	 * {@link Constants#CAPTCHA_TEXT_PRODUCER_FONT_NAMES} in the properties. If the
	 * property is not specified, it defaults to an array containing "Arial" and "Courier".
	 *
	 * @param fontSize The size of the font to be used.
	 * @return An array of {@link Font} objects based on the specified configuration or default fonts.
	 *
	 * @author LinhLH
	 * @since 19/04/2025
	 */
	public Font[] getTextProducerFonts(int fontSize) {
        String paramName = Constants.CAPTCHA_TEXT_PRODUCER_FONT_NAMES;

        String paramValue = this.properties.getProperty(paramName);

        return this.helper.getFonts(paramValue, fontSize, new Font[]{
                new Font("Arial", Font.BOLD, fontSize), new Font("Courier", Font.BOLD, fontSize)
        });
    }

	/**
	 * Retrieves the font size for the CAPTCHA text generation.
	 * <p>
	 * This method fetches the {@code "captcha.text-producer.font.size"} configuration property
	 * and ensures it's a positive integer using the {@code getPositiveInt} helper method.
	 * If the property is not specified or invalid, it defaults to {@code 40}.
	 *
	 * @return The font size for the CAPTCHA text as a positive integer; defaults to {@code 40}.
	 *
	 * @author LinhLH
	 * @since 19/04/2025
	 */
	public int getTextProducerFontSize() {
        String paramName = Constants.CAPTCHA_TEXT_PRODUCER_FONT_SIZE;

        String paramValue = this.properties.getProperty(paramName);

        return this.helper.getPositiveInt(paramName, paramValue, 40);
    }

	/**
	 * Retrieves the font color used for CAPTCHA text generation based on the configuration.
	 * <p>
	 * This method fetches the value of {@link Constants#CAPTCHA_TEXT_PRODUCER_FONT_COLOR}
	 * from the configuration properties and converts it into a {@link Color} object using
	 * the {@code getColor} helper method. If the property is not specified or invalid,
	 * it defaults to {@link Color#BLACK}.
	 *
	 * @return The {@link Color} object representing the font color for CAPTCHA text generation;
	 *         defaults to {@link Color#BLACK}.
	 *
	 * @author LinhLH
	 * @since 19/04/2025
	 */
	public Color getTextProducerFontColor() {
        String paramName = Constants.CAPTCHA_TEXT_PRODUCER_FONT_COLOR;

        String paramValue = this.properties.getProperty(paramName);

        return this.helper.getColor(paramName, paramValue, Color.BLACK);
    }

	/**
	 * Retrieves the character spacing for CAPTCHA text generation.
	 * <p>
	 * This method fetches the {@code "captcha.text-producer.char.space"} configuration property,
	 * ensures the value is a positive integer using the {@code getPositiveInt} helper method,
	 * and defaults to {@code 2} if the property is not specified or invalid.
	 *
	 * @return The character spacing for CAPTCHA text as a positive integer; defaults to {@code 2}.
	 *
	 * @author LinhLH
	 * @since 19/04/2025
	 */
	public int getTextProducerCharSpace() {
        String paramName = Constants.CAPTCHA_TEXT_PRODUCER_CHAR_SPACE;

        String paramValue = properties.getProperty(paramName);

        return this.helper.getPositiveInt(paramName, paramValue, 2);
    }

	/**
	 * Retrieves the implementation of the {@link NoiseProducer} configured for adding noise to CAPTCHA images.
	 * <p>
	 * This method fetches the "captcha.noise.impl" property from the configuration to dynamically determine the
	 * specific {@link NoiseProducer} implementation. If no custom implementation is specified or an error occurs
	 * during instantiation, a {@link DefaultNoise} instance is returned as the default.
	 *
	 * @return The {@link NoiseProducer} instance responsible for adding noise to CAPTCHA images.
	 *
	 * @author LinhLH
	 * @since 19/04/2025
	 */
	public NoiseProducer getNoiseImpl() {
        String paramName = Constants.CAPTCHA_NOISE_IMPL;

        String paramValue = this.properties.getProperty(paramName);

        return (NoiseProducer) this.helper.getClassInstance(paramName, paramValue,
                new DefaultNoise(), this);
    }

	/**
	 * Retrieves the noise color for the CAPTCHA configuration.
	 * <p>
	 * This method fetches the value of {@link Constants#CAPTCHA_NOISE_COLOR}
	 * from the configuration properties and converts it into a {@link Color}
	 * object using the {@code getColor} helper method. If the property is
	 * not specified or invalid, it defaults to {@link Color#BLACK}.
	 *
	 * @return The {@link Color} object representing the noise color for CAPTCHA
	 *         configuration; defaults to {@link Color#BLACK}.
	 */
	public Color getNoiseColor() {
        String paramName = Constants.CAPTCHA_NOISE_COLOR;
        String paramValue = this.properties.getProperty(paramName);
        return this.helper.getColor(paramName, paramValue, Color.BLACK);
    }

	/**
	 * This method retrieves the implementation of the GimpyEngine used for CAPTCHA obfuscation.
	 * It resolves the appropriate implementation based on the configured properties.
	 * <p>
	 * The method utilizes the configuration parameter {@code CAPTCHA_OBFUSCATE_IMPL} to obtain
	 * the specific engine class instance, falling back to the default {@link WaterRipple} if none is provided.
	 *
	 * @return an instance of {@link GimpyEngine} used for CAPTCHA obfuscation.
	 *
	 * @author LinhLH
	 * @since 19/04/2025
	 */
	public GimpyEngine getObfuscateImpl() {
        String paramName = Constants.CAPTCHA_OBFUSCATE_IMPL;

        String paramValue = this.properties.getProperty(paramName);

        return (GimpyEngine) this.helper.getClassInstance(paramName, paramValue, new WaterRipple(), this);
    }

	/**
	 * This method retrieves the implementation of the {@link WordRenderer}.
	 * It fetches the appropriate renderer instance by looking up the property
	 * configured for the specific implementation. If no specific implementation
	 * is found, a default {@link DefaultWordRenderer} is used.
	 *
	 * @return an instance of {@link WordRenderer} implementation based on configuration or default.
	 *
	 * @author LinhLH
	 * @since 19/04/2025
	 */
	public WordRenderer getWordRendererImpl() {
        String paramName = Constants.CAPTCHA_WORD_RENDERER_IMPL;

        String paramValue = this.properties.getProperty(paramName);

        return (WordRenderer) this.helper.getClassInstance(paramName, paramValue,
                new DefaultWordRenderer(), this);
    }

	/**
	 * This class provides functionalities for background implementation in captcha generation.
	 * The method retrieves a specific implementation of {@link BackgroundProducer} based on
	 * configuration properties and instantiates it.
	 * <p>
	 * The implementation is determined by the value of the property defined in {@link Constants#CAPTCHA_BACKGROUND_IMPL}.
	 *
	 * @author LinhLH
	 * @since 19/04/2025
	 */
	public BackgroundProducer getBackgroundImpl() {
        String paramName = Constants.CAPTCHA_BACKGROUND_IMPL;

        String paramValue = this.properties.getProperty(paramName);

        return (BackgroundProducer) this.helper.getClassInstance(paramName, paramValue,
                new DefaultBackground(), this);
    }

	/**
	 * Provides utility methods for handling background color configurations.
	 * <p>
	 * This class contains methods to retrieve and process background colors based
	 * on specific parameters, ensuring fallback to default values when needed.
	 * <p>
	 * All information regarding background colors is fetched and processed
	 * using specific constants and helper utilities for accuracy and ease
	 * of configurability.
	 *
	 * @author LinhLH
	 * @since 19/04/2025
	 */
	public Color getBackgroundColorFrom() {
        String paramName = Constants.CAPTCHA_BACKGROUND_CLR_FROM;

        String paramValue = this.properties.getProperty(paramName);

        return this.helper.getColor(paramName, paramValue, Color.LIGHT_GRAY);
    }

	/**
	 * This class is responsible for handling color configurations and retrieving
	 * background color settings for the CAPTCHA system.
	 * <p>
	 * It provides methods for interacting with predefined properties and
	 * determining colors based on them.
	 *
	 * @author LinhLH
	 * @since 19/04/2025
	 */
	public Color getBackgroundColorTo() {
        String paramName = Constants.CAPTCHA_BACKGROUND_CLR_TO;

        String paramValue = this.properties.getProperty(paramName);

        return this.helper.getColor(paramName, paramValue, Color.WHITE);
    }

	/**
	 * This class handles image properties and provides access to configuration
	 * values related to image dimensions, such as width.
	 * <p>
	 * It retrieves property values and ensures they conform to the expected format.
	 *
	 * @author LinhLH
	 * @since 19/04/2025
	 */
	public int getWidth() {
        String paramName = Constants.CAPTCHA_IMAGE_WIDTH;

        String paramValue = this.properties.getProperty(paramName);

        return this.helper.getPositiveInt(paramName, paramValue, 200);
    }

	/**
	 * This class is responsible for providing functionalities related to CAPTCHA image properties.
	 * <p>
	 * Allows retrieving the height of the CAPTCHA image based on configuration properties.
	 *
	 * @author LinhLH
	 * @since 19/04/2025
	 */
	public int getHeight() {
        String paramName = Constants.CAPTCHA_IMAGE_HEIGHT;
        String paramValue = this.properties.getProperty(paramName);
        return this.helper.getPositiveInt(paramName, paramValue, 50);
    }

    /**
     * Allows one to override the key name which is stored in the users
     * HttpSession. Defaults to Constants.CAPTCHA_SESSION_KEY.
     */
    public String getSessionKey() {
        return this.properties.getProperty(Constants.CAPTCHA_SESSION_CONFIG_KEY, Constants.CAPTCHA_SESSION_KEY);
    }

    /**
     * Allows one to override the date name which is stored in the
     * users HttpSession. Defaults to Constants.CAPTCHA_SESSION_KEY.
     */
    public String getSessionDate() {
        return this.properties.getProperty(Constants.CAPTCHA_SESSION_CONFIG_DATE, Constants.CAPTCHA_SESSION_DATE);
    }
}
