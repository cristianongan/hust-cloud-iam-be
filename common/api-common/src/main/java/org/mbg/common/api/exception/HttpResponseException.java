/**
 * 
 */
package org.mbg.common.api.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;

/**
 * @author LinhLH
 *
 */
@Getter
@Setter
@NoArgsConstructor
public class HttpResponseException extends RuntimeException {
	@Serial
	private static final long serialVersionUID = -3809480449274114180L;

	private int httpStatus;

	private String errorCode;

	private String errorMessage;
	
	private Object data;

	public HttpResponseException(int httpStatus, String errorCode, String errorMessage) {
		super(errorMessage);

		this.httpStatus = httpStatus;
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}

	public HttpResponseException(int httpStatus, String errorCode, String errorMessage, String message) {
		super(message);

		this.httpStatus = httpStatus;
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}
	
	/**
	 * @param httpStatus
	 * @param errorCode
	 * @param errorMessage
	 */
	public HttpResponseException(int httpStatus, String errorCode, String errorMessage, String message, Object data) {
		super(message);

		this.httpStatus = httpStatus;
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
		this.data = data;
	}

	public HttpResponseException(String message) {
		super(message);
	}

	public HttpResponseException(String message, Throwable t) {
		super(message, t);
	}
}
