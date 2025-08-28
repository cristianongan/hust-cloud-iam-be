package org.mbg.captcha.util;

import java.awt.Color;
import java.awt.Font;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;


/**
 * Utility class for handling common configuration tasks such as parsing colors,
 * integers, booleans, fonts, and class instances from configuration parameters.
 * <p>
 * This class provides a collection of helper methods to facilitate extracting and
 * validating configuration values from parameter input and converting them into
 * corresponding Java objects like {@link Color}, {@link Font}, and {@link Object}.
 * <p>
 * Invalid or malformed parameter values will result in a {@link ConfigException}.
 *
 * @author LinhLH
 * @since 19/04/2025
 */
public class ConfigHelper {
	/**
	 * Helper class for configuration-related operations.
	 * <p>
	 * This utility provides methods for handling configurations, including
	 * parsing color values from strings.
	 * </p>
	 *
	 * <p>Note: This class relies on standard Java color definitions and configurations.</p>
	 *
	 * @author LinhLH
	 * @since 19/04/2025
	 */
	public Color getColor(String paramName, String paramValue,
                          Color defaultColor) {
        Color color;
        if ("".equals(paramValue) || paramValue == null) {
            color = defaultColor;
        } else if (paramValue.indexOf(",") > 0) {
            color = createColorFromCommaSeparatedValues(paramName, paramValue);
        } else {
            color = createColorFromFieldValue(paramName, paramValue);
        }
        return color;
    }

	/**
	 * Creates a {@link Color} instance from a comma-separated string of values
	 * provided for the configuration parameter. The method supports both RGB and
	 * RGBA values.
	 * <p>
	 * This utility parses the input string, ensuring that it contains either
	 * three values (for RGB) or four values (for RGBA). If the input is invalid,
	 * it throws a {@link ConfigException}.
	 * </p>
	 *
	 * @param paramName the name of the configuration parameter being parsed
	 * @param paramValue the comma-separated string of color values to be parsed
	 *                   (e.g., "255,255,255" for RGB or "255,255,255,128" for RGBA)
	 * @return the {@link Color} object created from the parsed RGB(A) values
	 * @throws ConfigException if the input string is invalid or cannot be parsed
	 */
	public Color createColorFromCommaSeparatedValues(String paramName, String paramValue) {
        Color color;
        String[] colorValues = paramValue.split(",");
        try {
            int r = Integer.parseInt(colorValues[0]);
            int g = Integer.parseInt(colorValues[1]);
            int b = Integer.parseInt(colorValues[2]);
            if (colorValues.length == 4) {
                int a = Integer.parseInt(colorValues[3]);
                color = new Color(r, g, b, a);
            } else if (colorValues.length == 3) {
                color = new Color(r, g, b);
            } else {
                throw new ConfigException(paramName, paramValue,
                        "Color can only have 3 (RGB) or 4 (RGB with Alpha) values.");
            }
        } catch (ArrayIndexOutOfBoundsException | IllegalArgumentException nfe) {
            throw new ConfigException(paramName, paramValue, nfe);
        }
        return color;
    }

	/**
	 * Creates a {@link Color} object from a field name in {@link Color}.
	 * <p>
	 * This method attempts to resolve the given {@code paramValue} to a color by
	 * matching it with a declared constant in the {@link Color} class.
	 * If the field is not found, or if there is an error during resolution, a
	 * {@link ConfigException} is thrown.
	 * </p>
	 *
	 * @param paramName the name of the configuration parameter being resolved
	 * @param paramValue the field name in {@link Color} to retrieve the color
	 *                   (e.g., "RED" for {@link Color#RED})
	 * @return the {@link Color} object corresponding to the given field name
	 * @throws ConfigException if the field does not exist, or if there is another
	 *                         issue during resolution
	 * @since 19/04/2025
	 * @author LinhLH
	 */
	public Color createColorFromFieldValue(String paramName, String paramValue) {
        Color color;

        try {
            Field field = Class.forName("java.awt.Color").getField(paramValue);
            color = (Color) field.get(null);
        } catch (NoSuchFieldException | ClassNotFoundException | IllegalAccessException ex) {
            throw new ConfigException(paramName, paramValue, ex);
        }

        return color;
    }

	/**
	 * Helper class for configuration-related operations.
	 * <p>
	 * This utility provides methods for handling configurations, including
	 * creating instances using class names, handling colors, fonts, and other
	 * configuration parameters.
	 * </p>
	 * <p>
	 * Note: This class relies on standard Java configurations.
	 * </p>
	 *
	 * @author LinhLH
	 * @since 19/04/2025
	 */
    public Object getClassInstance(String paramName, String paramValue,
                                   Object defaultInstance, Config config) {
        Object instance;
        if ("".equals(paramValue) || paramValue == null) {
            instance = defaultInstance;
        } else {
            try {
                instance = Class.forName(paramValue).getDeclaredConstructor().newInstance();
            } catch (IllegalAccessException | ClassNotFoundException | InstantiationException iae) {
                throw new ConfigException(paramName, paramValue, iae);
            } catch (InvocationTargetException | NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }

        setConfigurable(instance, config);

        return instance;
    }

	/**
	 * Retrieves an array of {@link Font} objects based on the provided parameters.
	 * <p>
	 * This method creates an array of {@code Font} instances according to the specified font names,
	 * size, and bold style. If the provided {@code paramValue} is empty or null, the method returns
	 * the supplied {@code defaultFonts}.
	 * </p>
	 *
	 * @param paramValue  a comma-separated list of font names to use for creating the {@link Font} array.
	 *                    If null or empty, the {@code defaultFonts} parameter is used.
	 * @param fontSize    the size to assign to each created {@link Font}.
	 * @param defaultFonts an array of {@link Font} objects to return if {@code paramValue} is null or empty.
	 * @return an array of {@link Font} objects based on the provided parameters, or the {@code defaultFonts}
	 *         if no valid {@code paramValue} is provided.
	 * @since 19/04/2025
	 * @author LinhLH
	 */
	public Font[] getFonts(String paramValue, int fontSize,
                           Font[] defaultFonts) {
        Font[] fonts;
        if ("".equals(paramValue) || paramValue == null) {
            fonts = defaultFonts;
        } else {
            String[] fontNames = paramValue.split(",");
            fonts = new Font[fontNames.length];
            for (int i = 0; i < fontNames.length; i++) {
                fonts[i] = new Font(fontNames[i], Font.BOLD, fontSize);
            }
        }
        return fonts;
    }

	/**
	 * Parses a configuration parameter value and ensures it is a positive integer.
	 * <p>
	 * This method takes in a parameter name and its value in string format, along with a default
	 * integer value to return if the parameter does not exist or is invalid. If the parsed value
	 * is less than 1, or if the value cannot be parsed into an integer, a {@link ConfigException}
	 * is thrown.
	 * </p>
	 *
	 * @param paramName the name of the configuration parameter being parsed
	 * @param paramValue the string value of the parameter to be parsed as an integer
	 * @param defaultInt the default integer value to be returned if {@code paramValue} is null or empty
	 * @return the parsed positive integer value from {@code paramValue}, or {@code defaultInt} if
	 *         {@code paramValue} is null or empty
	 * @throws ConfigException if {@code paramValue} is not a valid integer or is less than 1
	 * @author LinhLH
	 * @since 19/04/2025
	 */
	public int getPositiveInt(String paramName, String paramValue,
                              int defaultInt) {
        int intValue;
        if ("".equals(paramValue) || paramValue == null) {
            intValue = defaultInt;
        } else {
            try {
                intValue = Integer.parseInt(paramValue);
                if (intValue < 1) {
                    throw new ConfigException(paramName, paramValue,
                            "Value must be greater than or equals to 1.");
                }
            } catch (NumberFormatException nfe) {
                throw new ConfigException(paramName, paramValue, nfe);
            }
        }
        return intValue;
    }

	/**
	 * Retrieves a character array based on the provided string parameter.
	 * <p>
	 * This method converts the given {@code paramValue} to a character array.
	 * If {@code paramValue} is null or empty, it returns the {@code defaultChars}.
	 * </p>
	 *
	 * @param paramValue   the string value to be converted into a character array.
	 *                     If null or empty, the {@code defaultChars} will be returned.
	 * @param defaultChars the default character array to be returned if
	 *                     {@code paramValue} is null or empty.
	 * @return a character array derived from {@code paramValue}, or
	 *         {@code defaultChars} if {@code paramValue} is null or empty.
	 */
	public char[] getChars(String paramValue, char[] defaultChars) {
        char[] chars;
        if ("".equals(paramValue) || paramValue == null) {
            chars = defaultChars;
        } else {
            chars = paramValue.toCharArray();
        }
        return chars;
    }

	/**
	 * Determines the boolean value of a configuration parameter.
	 * <p>
	 * This method evaluates the given {@code paramValue} to derive a boolean value.
	 * If {@code paramValue} is "yes", empty, or {@code null}, it returns the
	 * {@code defaultValue}. If it is "no", it explicitly returns {@code false}.
	 * Any other value results in a {@link ConfigException} being thrown.
	 * </p>
	 *
	 * @param paramName the name of the configuration parameter being parsed
	 * @param paramValue the value of the parameter to be interpreted as boolean
	 *                   (expected values: "yes", "no", empty, or {@code null})
	 * @param defaultValue a fallback boolean value to be used when {@code paramValue}
	 *                     is empty or {@code null}
	 * @return the derived boolean value based on the {@code paramValue}, or the
	 *         {@code defaultValue} for empty or {@code null} values
	 * @throws ConfigException if {@code paramValue} is not "yes", "no", empty, or {@code null}
	 * @author LinhLH
	 * @since 19/04/2025
	 */
	public boolean getBoolean(String paramName, String paramValue, boolean defaultValue) {
        boolean booleanValue;
        if ("yes".equals(paramValue) || "".equals(paramValue)
                || paramValue == null) {
            booleanValue = defaultValue;
        } else if ("no".equals(paramValue)) {
            booleanValue = false;
        } else {
            throw new ConfigException(paramName, paramValue,
                    "Value must be either yes or no.");
        }
        return booleanValue;
    }

	/**
	 * Sets the configuration for an object implementing the {@link Configurable} interface.
	 * <p>
	 * This method checks if the provided object is an instance of {@link Configurable}.
	 * If the check succeeds, it applies the given configuration to the object using
	 * the {@code setConfig} method of the {@link Configurable} interface.
	 * </p>
	 *
	 * @param object the object to be configured, which should implement the {@link Configurable} interface
	 * @param config the {@link Config} instance to be applied to the object
	 */
	private void setConfigurable(Object object, Config config) {
        if (object instanceof Configurable) {
            ((Configurable) object).setConfig(config);
        }
    }
}
