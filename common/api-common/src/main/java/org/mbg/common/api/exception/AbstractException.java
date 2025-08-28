package org.mbg.common.api.exception;

import org.mbg.common.api.util.ApiConstants;
import lombok.Getter;
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

    private final String entityName;

    private final String errorKey;

    public AbstractException(String defaultMessage, String entityName, Status status, String errorKey) {
        this(ApiConstants.ErrorType.DEFAULT_TYPE, defaultMessage, status, entityName, errorKey);
    }

    public AbstractException(URI type, String defaultMessage, Status status, String entityName, String errorKey) {
        super(type, defaultMessage, status, null, null, null,
                getAlertParameters(defaultMessage, entityName, errorKey));

        this.entityName = entityName;
        this.errorKey = errorKey;
    }

    public AbstractException(String defaultMessage, Status status, String entityName, String errorKey,
                               Map<String, Object> params, Map<String, Object> values) {
        super(ApiConstants.ErrorType.DEFAULT_TYPE, defaultMessage, status, null, null, null, params);

        params.put(ApiConstants.ErrorKey.MESSAGE, defaultMessage);
        params.put(ApiConstants.ErrorKey.ERROR_KEY, errorKey);

        if (values != null && !values.isEmpty()) {
            params.put(ApiConstants.ErrorKey.PARAMS, values);
        }

        this.entityName = entityName;
        this.errorKey = errorKey;
    }

    public AbstractException(String defaultMessage, Status status, String entityName, String errorKey,
                               Map<String, Object> params) {
        super(ApiConstants.ErrorType.DEFAULT_TYPE, defaultMessage, status, null, null, null, params);

        params.put(ApiConstants.ErrorKey.MESSAGE, defaultMessage);
        params.put(ApiConstants.ErrorKey.ERROR_KEY, errorKey);

        this.entityName = entityName;
        this.errorKey = errorKey;
    }

    private static Map<String, Object> getAlertParameters(String message, String entityName, String errorKey) {
        Map<String, Object> parameters = new HashMap<>();

        parameters.put(ApiConstants.ErrorKey.MESSAGE, message);
        parameters.put(ApiConstants.ErrorKey.ERROR_KEY, errorKey);
        parameters.put(ApiConstants.ErrorKey.PARAMS, entityName);

        return parameters;
    }
}
