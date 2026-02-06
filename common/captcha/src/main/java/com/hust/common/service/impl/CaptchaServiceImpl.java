package com.hust.common.service.impl;

import com.hust.captcha.Producer;
import com.hust.common.dto.CaptchaDTO;
import com.hust.common.repository.CaptchaRepository;
import com.hust.common.service.CaptchaService;
import com.hust.common.util.FileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Service implementation for handling CAPTCHA operations.
 * <p>
 * This class implements the {@link CaptchaService} interface to provide functionalities
 * for generating, validating, and managing CAPTCHAs.
 * <p>
 * It leverages dependencies such as a CAPTCHA producer, password encoder,
 * and a CAPTCHA repository to ensure secure and efficient CAPTCHA handling.
 *
 * <p>
 * The implementation includes methods for generating CAPTCHA text and images,
 * returning CAPTCHA-related data required for client-side rendering,
 * and validating user-provided input against stored CAPTCHA values.
 * <p>
 * This service is designed to enhance security measures, such as preventing
 * automated bots and enabling secure authentication.
 *
 * @author LinhLH
 * @since 19/04/2025
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CaptchaServiceImpl implements CaptchaService {

	private final Producer captchaProducer;

	private final PasswordEncoder encoder;

	private final CaptchaRepository captchaRepository;

	@Override
	public CaptchaDTO generate() {
		String capText = this.captchaProducer.createText();

		String transactionId = this.encoder.encode(capText);

		this.captchaRepository.put(transactionId, capText);
		// create the image with the text
		BufferedImage bi = this.captchaProducer.createImage(capText);

		return new CaptchaDTO(transactionId, FileUtil.getImageSrcBase64String(bi, "jpg"));
	}

	@Override
	public Map<String, Object> generateRequired() {
		String capText = this.captchaProducer.createText();

		String transactionId = this.encoder.encode(capText);

		this.captchaRepository.put(transactionId, capText);
		// create the image with the text
		BufferedImage bi = this.captchaProducer.createImage(capText);

		Map<String, Object> data = new HashMap<>();

		data.put("captcha", FileUtil.getImageSrcBase64String(bi, "jpg"));
		data.put("transactionId", transactionId);
		data.put("captchaRequired", true);

		return data;
	}

	public boolean isInvalidCaptcha(String transactionId, String inputCaptcha) {
		String captchaInCache = this.captchaRepository.getIfPresent(transactionId);

		if (Objects.nonNull(captchaInCache) && captchaInCache.equals(inputCaptcha)) {
			// invalidate captcha
			this.captchaRepository.invalidate(transactionId);

			return false;
		}

		return true;
	}
}
