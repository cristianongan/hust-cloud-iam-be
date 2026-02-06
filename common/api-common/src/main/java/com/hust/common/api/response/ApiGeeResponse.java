package com.hust.common.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class ApiGeeResponse implements Serializable {

	@Serial
	private static final long serialVersionUID = 5831861618277831329L;

	private String clientMessageId;

	private String errorCode;

	private List<String> errorDesc = new ArrayList<>();

	private String errorDetail;
	
	public abstract Object getData();
}
