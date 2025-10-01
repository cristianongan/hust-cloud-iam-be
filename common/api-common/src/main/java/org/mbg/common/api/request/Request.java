package org.mbg.common.api.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpMethod;

import jakarta.persistence.Transient;
import java.io.Serial;
import java.io.Serializable;

/**
 * @author LinhLH
 *
 */
@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@SuperBuilder
@JsonIgnoreProperties(ignoreUnknown = true, value = {"clientMessageId", "requestLog", "baseUrl", "requestURI", "method"})
public abstract class Request implements Serializable {
	@Serial
	private static final long serialVersionUID = -8440513573690364524L;

	/**
	 * @param baseUrl the base URL
	 * @param requestURI the request URI
	 * @param method the request method
	 */
	public Request(String baseUrl, String requestURI, HttpMethod method) {
		super();
		this.baseUrl = baseUrl;
		this.requestURI = requestURI;
		this.method = method;
	}

	@JsonIgnore
	private transient String clientMessageId;

	@JsonIgnore
	@Transient
	private transient Object requestLog;

	@JsonIgnore
	@Transient
	private transient String baseUrl;

	@JsonIgnore
	@Transient
	private transient String requestURI;

	@JsonIgnore
	@Transient
	private transient HttpMethod method = HttpMethod.GET;

	@JsonIgnore
    @Transient
	public String getRequestURL() {
		return this.baseUrl + this.requestURI;
	}

}
