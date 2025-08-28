package org.mbg.common.aop;

import org.mbg.common.annotation.Signature;
import org.mbg.common.api.exception.BadRequestException;
import org.mbg.common.api.util.ApiConstants;
import org.mbg.common.label.LabelKey;
import org.mbg.common.label.Labels;
import org.mbg.common.security.RsaProvider;
import org.mbg.common.util.AnnotationConstants;
import org.mbg.common.util.Validator;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * An aspect used for intercepting and verifying the integrity of requests annotated with {@link Signature}.
 * This aspect ensures the validity of the provided signature in the HTTP request headers
 * by comparing it with the decrypted value of the transaction ID.
 * <p>
 * The class relies on RSA cryptographic mechanisms provided by {@link RsaProvider} to verify the signature.
 * In case of an incorrect or missing signature, a {@link BadRequestException} is thrown.
 * <p>
 * This class is part of the application's security mechanism for validating secure transactions.
 *
 * @author LinhLH
 * @since October 31, 2023
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class SignatureAspect {
	private final RsaProvider signalRsaProvider;

    /**
     * Intercepts a method annotated with {@link Signature} to validate the integrity
     * of HTTP requests by checking the provided signature in the HTTP headers.
     * <p>
     * The method retrieves request attributes and extracts the transaction ID and signature
     * from the headers. It then decrypts the signature using an RSA provider and compares it
     * to the transaction ID. If the signature is invalid, missing, or the decrypted signature
     * does not match the transaction ID, a {@link BadRequestException} is thrown.
     * <p>
     * This validation ensures that only requests with a valid transaction signature can proceed.
     * <p>
     *
     * @throws BadRequestException if the signature is missing, invalid, or does not match the transaction ID.
     * @throws Throwable any unexpected exception that occurs during the operation.
     */
	@Before("@annotation(com.vmc.common.annotation.Signature)")
	public void requiredKaptcha() throws Throwable {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();

        if (requestAttributes == null) {
            throw new BadRequestException(Labels.getLabels(LabelKey.ERROR_INCORRECT_SIGNATURE),
                            AnnotationConstants.EntityName.SIGNATURE, LabelKey.ERROR_INCORRECT_SIGNATURE);
        }

        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();

        String transactionId = request.getHeader(ApiConstants.HttpHeaders.X_TRANSACTION_ID);
        String signature = request.getHeader(ApiConstants.HttpHeaders.X_SIGNATURE);

        _log.info("signature: {}, transactionId: {}", signature, transactionId);

        if (Validator.isNull(signature) || Validator.isNull(transactionId)) {
            throw new BadRequestException(Labels.getLabels(LabelKey.ERROR_INCORRECT_SIGNATURE),
                            AnnotationConstants.EntityName.SIGNATURE, LabelKey.ERROR_INCORRECT_SIGNATURE);
        }

        String transactionIdDecode = this.signalRsaProvider.decrypt(signature);

        if (!transactionIdDecode.equals(transactionId)) {
            throw new BadRequestException(Labels.getLabels(LabelKey.ERROR_INCORRECT_SIGNATURE),
                            AnnotationConstants.EntityName.SIGNATURE, LabelKey.ERROR_INCORRECT_SIGNATURE);
        }
	}
}
