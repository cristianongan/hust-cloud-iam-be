package org.mbg.captcha.servlet;

import org.mbg.captcha.Constants;
import org.mbg.captcha.Producer;
import org.mbg.captcha.util.Config;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

/**
 * The {@code CaptchaExtend} class is responsible for generating CAPTCHA images and managing
 * CAPTCHA-related session data. It integrates with the Google CAPTCHA (captcha) library to create
 * images containing randomly generated text for security or validation purposes in web applications.
 *
 * <p>This class includes functionality to:
 * <p>
 * <ul>
 *   <li>Generate CAPTCHA images.</li>
 *   <li>Store the CAPTCHA text and its corresponding timestamp in session attributes.</li>
 * </ul>
 *
 * <p>Note that the generated CAPTCHA image prevents caching to ensure a unique image is created
 * every time.
 *
 * <p>Configuration properties are set to customize the CAPTCHA generation.
 *
 * @author LinhLH
 * @since 19/04/2025
 */
public class CaptchaExtend {

    private Producer captchaProducer = null;

	private String sessionKeyValue = null;

	private String sessionKeyDateValue = null;

	public CaptchaExtend() {
		// Switch off disk based caching.
		ImageIO.setUseCache(false);

        Properties props = new Properties();

        props.put("captcha.border", "no");
		props.put("captcha.text-producer.font.color", "black");
		props.put("captcha.text-producer.char.space", "5");

		Config config = new Config(props);

		this.captchaProducer = config.getProducerImpl();
		this.sessionKeyValue = config.getSessionKey();
		this.sessionKeyDateValue = config.getSessionDate();
	}

	/**
	 * Generates and outputs a CAPTCHA image in response to an HTTP request. This method sets the
	 * appropriate response headers for no caching and produces a JPEG format image with a randomly
	 * generated CAPTCHA text. Additionally, the generated CAPTCHA text and timestamp are stored as
	 * session attributes for further validation.
	 *
	 * <p>Note that the session attributes are updated after the image is generated to ensure data
	 * integrity even if image generation fails.
	 *
	 * @param req  the {@link HttpServletRequest} object that contains the request the client has made
	 *             to the servlet, used to manage session attributes for the generated CAPTCHA text and
	 *             timestamp
	 * @param resp the {@link HttpServletResponse} object that contains the response the servlet sends
	 *             to the client, used to set response headers and output the CAPTCHA image as JPEG
	 * @throws ServletException if the request could not be handled
	 * @throws IOException      if an input or output error is detected when the servlet handles the
	 *                          request
	 */
	public void captcha(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// Set standard HTTP/1.1 no-cache headers.
		resp.setHeader("Cache-Control", "no-store, no-cache");

		// return a jpeg
		resp.setContentType("image/jpeg");

		// create the text for the image
		String capText = this.captchaProducer.createText();

		// store the text in the session
		req.getSession().setAttribute(this.sessionKeyValue, capText);

		// store the date in the session so that it can be compared
		// against to make sure someone hasn't taken too long to enter
		// their captcha
		req.getSession().setAttribute(this.sessionKeyDateValue, new Date());

		// create the image with the text
		BufferedImage bi = this.captchaProducer.createImage(capText);

		ServletOutputStream out = resp.getOutputStream();

		// write the data out
		ImageIO.write(bi, "jpg", out);

		// fixes issue #69: set the attributes after we write the image in case
		// the image writing fails.

		// store the text in the session
		req.getSession().setAttribute(this.sessionKeyValue, capText);

		// store the date in the session so that it can be compared
		// against to make sure someone hasn't taken too long to enter
		// their captcha
		req.getSession().setAttribute(this.sessionKeyDateValue, new Date());
	}

	public String getGeneratedKey(HttpServletRequest req) {
		HttpSession session = req.getSession();
		return (String) session
				.getAttribute(Constants.CAPTCHA_SESSION_KEY);
	}
}
