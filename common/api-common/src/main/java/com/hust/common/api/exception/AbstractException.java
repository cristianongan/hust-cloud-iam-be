package com.hust.common.api.exception;

import com.hust.common.api.util.ApiConstants;
import lombok.Getter;
import com.hust.common.label.Labels;
import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.io.Serial;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: LinhLH
 **/
@Getter
public class AbstractException extends AbstractThrowableProblem {

    @Serial
    private static final long serialVersionUID = -7499406014900417719L;

    private final String reasonCode;

    private final String errorKey;

    public AbstractException(String defaultMessage, String reasonCode, Status status, String errorKey) {
        this(ApiConstants.ErrorType.DEFAULT_TYPE, defaultMessage, status, reasonCode, errorKey);
    }

    public AbstractException(URI type, String defaultMessage, Status status, String reasonCode, String errorKey) {
        super(type, defaultMessage, status, null, null, null,
                getAlertParameters(defaultMessage, reasonCode, errorKey));

        this.reasonCode = reasonCode;
        this.errorKey = errorKey;
    }

    public AbstractException(String defaultMessage, Status status, String reasonCode, String errorKey,
                               Map<String, Object> params, Map<String, Object> values) {
        super(ApiConstants.ErrorType.DEFAULT_TYPE, defaultMessage, status, null, null, null, params);

        params.put(ApiConstants.ErrorKey.MESSAGE, Labels.getLabels(defaultMessage));
        params.put(ApiConstants.ErrorKey.ERROR_KEY, errorKey);

        if (values != null && !values.isEmpty()) {
            params.put(ApiConstants.ErrorKey.PARAMS, values);
        }

        this.reasonCode = reasonCode;
        this.errorKey = errorKey;
    }

    public AbstractException(String defaultMessage, Status status, String reasonCode, String errorKey,
                               Map<String, Object> params) {
        super(ApiConstants.ErrorType.DEFAULT_TYPE, defaultMessage, status, null, null, null, params);

        params.put(ApiConstants.ErrorKey.MESSAGE, Labels.getLabels(defaultMessage));
        params.put(ApiConstants.ErrorKey.ERROR_KEY, errorKey);

        this.reasonCode = reasonCode;
        this.errorKey = errorKey;
    }

    private static Map<String, Object> getAlertParameters(String message, String entityName, String errorKey) {
        Map<String, Object> parameters = new HashMap<>();

        parameters.put(ApiConstants.ErrorKey.MESSAGE, Labels.getLabels(message));
        parameters.put(ApiConstants.ErrorKey.ERROR_KEY, errorKey);
        parameters.put(ApiConstants.ErrorKey.PARAMS, entityName);

        return parameters;
    }
}
