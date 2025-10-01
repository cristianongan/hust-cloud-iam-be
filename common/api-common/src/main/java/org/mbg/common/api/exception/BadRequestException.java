package org.mbg.common.api.exception;

import org.mbg.common.api.util.ApiConstants;
import lombok.Getter;
import org.mbg.common.base.enums.ErrorCode;
import org.mbg.common.label.Labels;
import org.zalando.problem.Status;

import java.io.Serial;
import java.net.URI;
import java.util.Map;

@Getter
public class BadRequestException extends AbstractException {

	@Serial
	private static final long serialVersionUID = 165464651313156L;

	public BadRequestException(String defaultMessage, String entityName, String errorKey) {
		this(ApiConstants.ErrorType.DEFAULT_TYPE, defaultMessage, entityName, errorKey);
	}

	public BadRequestException(String defaultMessage, String entityName, String errorKey,
			Map<String, Object> params, Map<String, Object> values) {
		super(defaultMessage, Status.BAD_REQUEST, entityName, errorKey, params, values);
	}
	
	public BadRequestException(String defaultMessage, String entityName, String errorKey,
			Map<String, Object> params) {
		super(defaultMessage, Status.BAD_REQUEST, entityName, errorKey, params);
	}

    public BadRequestException(URI type, String defaultMessage, String entityName, String errorKey) {
        super(type, defaultMessage, Status.BAD_REQUEST, entityName, errorKey);
    }

	public BadRequestException(ErrorCode errorCode) {
		this(Labels.getLabels(errorCode.getKey()), errorCode.name(), errorCode.getKey());
	}
}
