package com.hust.captcha.servlet;

import com.hust.captcha.Producer;
import com.hust.captcha.util.Config;
import jakarta.servlet.Servlet;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serial;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;

/**
 * The {@code CaptchaServlet} class is responsible for generating CAPTCHA images and handling the
 * interaction via HTTP requests. It creates image-based CAPTCHAs containing randomly generated
 * text for security purposes in web applications.
 *
 * <p>Key functionalities include:
 * <p>
 * <ul>
 *   <li>Processing initialization parameters for CAPTCHA customization using {@link Producer}.</li>
 *   <li>Generating CAPTCHA images with the text visually distorted.</li>
 *   <li>Storing generated CAPTCHA text and timestamps in the HTTP session for validation.</li>
 * </ul>
 *
 * <p>The class ensures that CAPTCHAs are not cached to prevent reuse and maintains security integrity.
 * Configuration settings for CAPTCHA generation are provided via servlet initialization parameters.
 *
 * <p>The implementation uses Java Servlet API and adheres to standard HTTP response specifications.
 *
 * <p>Note: This class overrides {@code init()} to load configurations, and {@code doGet()} to generate
 * and serve CAPTCHA images to the client.
 *
 * @author LinhLH
 * @since 19/04/2025
 */
public class CaptchaServlet extends HttpServlet implements Servlet {
    @Serial
    private static final long serialVersionUID = -8012197599817281707L;

    private Properties props = new Properties();

    private Producer captchaProducer = null;

    private String sessionKeyValue = null;

    private String sessionKeyDateValue = null;

    /*
     * (non-Javadoc)
     *
     * @see javax.servlet.Servlet#init(javax.servlet.ServletConfig)
     */
    @Override
    public void init(ServletConfig conf) throws ServletException {
        super.init(conf);

        // Switch off disk based caching.
        ImageIO.setUseCache(false);

        Enumeration<?> initParams = conf.getInitParameterNames();
        while (initParams.hasMoreElements()) {
            String key = (String) initParams.nextElement();
            String value = conf.getInitParameter(key);
            this.props.put(key, value);
        }

        Config config = new Config(this.props);

        this.captchaProducer = config.getProducerImpl();
        this.sessionKeyValue = config.getSessionKey();
        this.sessionKeyDateValue = config.getSessionDate();
    }

	/**
	 * Handles HTTP GET requests to generate and return a CAPTCHA image as a response.
	 * This method ensures the CAPTCHA text is stored in session attributes and sets
	 * response headers to prevent caching.
	 *
	 * <p>The generated CAPTCHA image is of JPEG format, and the process involves
	 * generating a random distorted text and an image representing that text. Additionally,
	 * the session attributes are updated after the image is written to the output stream
	 * to ensure data consistency even in case of a failure during image generation.
	 *
	 * <p>Note: This method is part of the {@code CaptchaServlet} class.
	 *
	 * @param req  the {@link HttpServletRequest} object containing the client request. Used to store
	 *             the generated CAPTCHA text and a timestamp in the session.
	 * @param resp the {@link HttpServletResponse} object used to configure headers and send the
	 *             generated CAPTCHA image as a response to the client.
	 * @throws ServletException if the processing of the request fails
	 * @throws IOException      if there is an error in writing the image to the output stream
	 *
	 * @author LinhLH
	 * @since 19/04/2025
	 */
	@Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // Set standard HTTP/1.1 no-cache headers.
        resp.setHeader("Cache-Control", "no-store, no-cache");

        // return a jpeg
        resp.setContentType("image/jpeg");

        // create the text for the image
        String capText = this.captchaProducer.createText();

        // create the image with the text
        BufferedImage bi = this.captchaProducer.createImage(capText);

        ServletOutputStream out = resp.getOutputStream();

        // write the data out
        ImageIO.write(bi, "jpg", out);

        // fixes issue #69: set the attributes after we write the image in case the image writing fails.

        // store the text in the session
        req.getSession().setAttribute(this.sessionKeyValue, capText);

        // store the date in the session so that it can be compared
        // against to make sure someone hasn't taken too long to enter
        // their captcha
        req.getSession().setAttribute(this.sessionKeyDateValue, new Date());
    }
}
