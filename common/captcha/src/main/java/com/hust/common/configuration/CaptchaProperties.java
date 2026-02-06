package com.hust.common.configuration;

import com.hust.captcha.Constants;
import com.hust.captcha.Producer;
import com.hust.captcha.impl.DefaultCaptcha;
import com.hust.captcha.util.Config;
import com.hust.common.util.CaptchaConstants;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * A configuration properties class for setting up CAPTCHA images using various properties.
 * This class is annotated as a Spring component with configuration properties automatically mapped from `captcha.config` prefix.
 * <p>
 * The class provides customization of CAPTCHA behavior such as image dimensions, text producers, background and border styles,
 * and font configurations.
 * <p>
 * This includes a method to create and configure a `Producer` bean for CAPTCHA generation.
 * <p>
 * Note: The inner members and methods are not included in this documentation.
 *
 * <p>
 * Dependencies:
 * <ul>
 *   <li>Spring Boot `ConfigurationProperties` annotation for mapping properties.</li>
 *   <li>A CAPTCHA library for generating CAPTCHA images dynamically using the provided configuration.</li>
 * </ul>
 *
 * @author LinhLH
 * @since 19/04/2025
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = CaptchaConstants.PropertyPrefix.CAPTCHA_CONFIG)
public class CaptchaProperties {

	private String imageWidth;

	private String imageHeight;

	private String textProducerCharString;

	private String textProducerCharLength;

	private String textProducerFontSize;

	private String textProducerCharSpace;

	private String textProducerFontNames;

	private String textProducerFontColor;

	private String backgroundClearFrom;

	private String backgroundClearTo;

	private String headerName;

	private String useBorder;

	@Bean
	public Producer createCaptchaProducer() {
		DefaultCaptcha captcha = new DefaultCaptcha();

		Properties properties = new Properties();

		properties.put(Constants.CAPTCHA_IMAGE_HEIGHT, this.imageHeight);
		properties.put(Constants.CAPTCHA_IMAGE_WIDTH, this.imageWidth);
		properties.put(Constants.CAPTCHA_TEXT_PRODUCER_CHAR_LENGTH, this.textProducerCharLength);
		properties.put(Constants.CAPTCHA_TEXT_PRODUCER_CHAR_STRING, this.textProducerCharString);
		properties.put(Constants.CAPTCHA_TEXT_PRODUCER_FONT_SIZE, this.textProducerFontSize);
		properties.put(Constants.CAPTCHA_TEXT_PRODUCER_CHAR_SPACE, this.textProducerCharSpace);
		properties.put(Constants.CAPTCHA_TEXT_PRODUCER_FONT_NAMES, this.textProducerFontNames);
		properties.put(Constants.CAPTCHA_TEXT_PRODUCER_FONT_COLOR, this.textProducerFontColor);
		properties.put(Constants.CAPTCHA_BACKGROUND_CLR_FROM, this.backgroundClearFrom);
		properties.put(Constants.CAPTCHA_BACKGROUND_CLR_TO, this.backgroundClearTo);
		properties.put(Constants.CAPTCHA_BORDER, this.useBorder);

		properties.put(Constants.CAPTCHA_NOISE_COLOR, this.textProducerFontColor);

		captcha.setConfig(new Config(properties));

		return captcha;
	}
}
