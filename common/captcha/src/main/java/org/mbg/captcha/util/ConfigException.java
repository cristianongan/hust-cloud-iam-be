package org.mbg.captcha.util;

import java.io.Serial;

/**
 * Represents an exception that is thrown when a configuration parameter
 * has an invalid value.
 * <p>
 * This exception is used to signal issues with configuration values provided
 * for specific parameters, either in terms of invalid content or other related
 * issues that prevent proper operation.
 *
 * @author LinhLH
 * @since 19/04/2025
 */
public class ConfigException extends RuntimeException {
    @Serial
	private static final long serialVersionUID = 6937416954897707291L;

    public ConfigException(String paramName, String paramValue, Throwable cause) {
        super("Invalid value '" + paramValue + "' for config parameter '"
                + paramName + "'.", cause);
    }

    public ConfigException(String paramName, String paramValue, String message) {
        super("Invalid value '" + paramValue + "' for config parameter '"
                + paramName + "'. " + message);
    }
}
