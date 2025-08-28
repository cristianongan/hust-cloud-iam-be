package org.mbg.common.api.response;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse {

	@SerializedName(value = "code")
	private int status;

	@SerializedName(value = "error_message", alternate = {"error_description"})
	private String message;

	@SerializedName(value = "error_code", alternate = {"error"})
	private String code;
}
