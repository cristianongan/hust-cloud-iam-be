package org.mbg.common.api.exception;

import org.mbg.common.api.util.ApiConstants;
import lombok.Getter;
import org.zalando.problem.Status;

import java.io.Serial;
import java.net.URI;
import java.util.Map;

@Getter
public class InternalServerErrorException extends AbstractException {

	@Serial
	private static final long serialVersionUID = 1657673413131654L;

	public InternalServerErrorException(String defaultMessage, String entityName, String errorKey) {
		this(ApiConstants.ErrorType.DEFAULT_TYPE, defaultMessage, entityName, errorKey);
	}

	public InternalServerErrorException(String defaultMessage, String entityName, String errorKey,
							   Map<String, Object> params, Map<String, Object> values) {
		super(defaultMessage, Status.BAD_REQUEST, entityName, errorKey, params, values);
	}

	public InternalServerErrorException(String defaultMessage, String entityName, String errorKey,
							   Map<String, Object> params) {
		super(defaultMessage, Status.BAD_REQUEST, entityName, errorKey, params);
	}

	public InternalServerErrorException(URI type, String defaultMessage, String entityName, String errorKey) {
		super(type, defaultMessage, Status.BAD_REQUEST, entityName, errorKey);
	}

}
