package org.mbg.common.api.advice;

import org.mbg.common.api.exception.BadRequestException;
import org.mbg.common.api.exception.ClientResponseException;
import org.mbg.common.api.exception.HttpResponseException;
import org.mbg.common.api.exception.InternalServerErrorException;
import org.mbg.common.base.enums.ErrorCode;
import org.mbg.common.security.exception.NoPermissionException;
import org.mbg.common.api.util.ApiConstants;
import org.mbg.common.api.util.HeaderUtil;
import org.mbg.common.label.LabelKey;
import org.mbg.common.label.Labels;
import org.mbg.common.security.exception.UnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.NativeWebRequest;
import org.zalando.problem.DefaultProblem;
import org.zalando.problem.Problem;
import org.zalando.problem.ProblemBuilder;
import org.zalando.problem.Status;
import org.zalando.problem.StatusType;
import org.zalando.problem.spring.web.advice.ProblemHandling;
import org.zalando.problem.spring.web.advice.security.SecurityAdviceTrait;
import org.zalando.problem.violations.ConstraintViolationProblem;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Controller advice to translate the server side exceptions to client-friendly
 * json structures. The error response follows RFC7807 - Problem Details for
 * HTTP APIs (<a href="https://tools.ietf.org/html/rfc7807">...</a>).
 */
@RestControllerAdvice
public class ExceptionTranslator implements ProblemHandling, SecurityAdviceTrait {
	/**
	 * Post-process the Problem payload to add the message key for the front-end if
	 * needed.
	 */
	@Override
	public ResponseEntity<Problem> process(@Nullable ResponseEntity<Problem> entity, NativeWebRequest request) {
		if (entity == null) {
			return null;
		}

		Problem problem = entity.getBody();

		if (!(problem instanceof ConstraintViolationProblem || problem instanceof DefaultProblem)) {
			return entity;
		}

		ProblemBuilder builder = Problem.builder()
				.withType(Problem.DEFAULT_TYPE.equals(problem.getType()) ? ApiConstants.ErrorType.DEFAULT_TYPE
						: problem.getType())
				.withStatus(problem.getStatus()).withTitle(problem.getTitle())
				.with(ApiConstants.ErrorKey.PATH, Objects.requireNonNull(request.getNativeRequest(HttpServletRequest.class)).getRequestURI());

		if (problem instanceof ConstraintViolationProblem) {
			builder.with(ApiConstants.ErrorKey.VIOLATIONS, ((ConstraintViolationProblem) problem).getViolations())
					.with(ApiConstants.ErrorKey.MESSAGE, LabelKey.ERROR_CONSTRAINT_VIOLATION);
		} else {
			builder.withCause(((DefaultProblem) problem).getCause())
					//.withDetail(problem.getDetail())
					.withInstance(problem.getInstance());
			
			problem.getParameters().forEach(builder::with);
			
			if (!problem.getParameters().containsKey(ApiConstants.ErrorKey.MESSAGE) && problem.getStatus() != null) {
				builder.with(ApiConstants.ErrorKey.MESSAGE, "error.http." + problem.getStatus().getStatusCode());
			}
		}

		return new ResponseEntity<>(builder.build(), entity.getHeaders(), entity.getStatusCode());
	}

	@Override
	public ResponseEntity<Problem> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			@Nonnull NativeWebRequest request) {
		BindingResult result = ex.getBindingResult();

		List<FieldError> fieldErrors = result.getFieldErrors().stream()
				.map(f -> new FieldError(f.getObjectName().replaceFirst("DTO$", ""), f.getField(), f.getCode()))
				.collect(Collectors.toList());

		Problem problem = Problem.builder().withType(ApiConstants.ErrorType.CONSTRAINT_VIOLATION_TYPE)
				.withTitle(Labels.getLabels(LabelKey.ERROR_METHOD_ARGUMENT_NOT_VALID))
				.withStatus(defaultConstraintViolationStatus())
				.with(ApiConstants.ErrorKey.MESSAGE, Labels.getLabels(LabelKey.ERROR_METHOD_ARGUMENT_NOT_VALID))
				.with(ApiConstants.ErrorKey.FIELD_ERRORS, fieldErrors).build();

		return create(ex, problem, request);
	}

	@ExceptionHandler
	public ResponseEntity<Problem> handleBadRequestAlertException(BadRequestException ex, NativeWebRequest request) {
		return create(ex, request,
				HeaderUtil.createFailureAlert(true, ex.getReasonCode(), ex.getErrorKey(), ex.getMessage()));


	}
	
	@ExceptionHandler
	public ResponseEntity<Problem> handleHttpResponseException(HttpResponseException ex, NativeWebRequest request) {
		Problem problem = Problem.builder().withStatus(new StatusType() {

			@Override
			public int getStatusCode() {
				return ex.getHttpStatus();
			}

			@Override
			public String getReasonPhrase() {
				return ex.getErrorMessage();
			}
		}).with(ApiConstants.ErrorKey.MESSAGE, ex.getMessage())
				.with(ApiConstants.ErrorKey.ERROR_KEY, ex.getErrorMessage())
				.with(ApiConstants.ErrorKey.ERROR_CODE, ex.getErrorCode())
				.with(ApiConstants.ErrorKey.DATA, ex.getData())
				.build();

		return create(ex, problem, request);
	}
	
	@ExceptionHandler
	public ResponseEntity<Problem> handleNoPermissionException(NoPermissionException ex, NativeWebRequest request) {
		Problem problem =
				Problem.builder().withStatus(Status.FORBIDDEN)
						.with(ApiConstants.ErrorKey.REASON_CODE, ErrorCode.MSG1032.name())
						.with(ApiConstants.ErrorKey.MESSAGE,
								Labels.getLabels(LabelKey.ERROR_YOU_MIGHT_NOT_HAVE_PERMISSION_TO_PERFORM_THIS_ACTION))
						.build();

		return create(ex, problem, request);
	}
	
	@ExceptionHandler
	public ResponseEntity<Problem> handleHttpClientErrorException(HttpClientErrorException ex,
			NativeWebRequest request) {
		final HttpStatusCode status = ex.getStatusCode();

		Problem problem = Problem.builder().withStatus(new StatusType() {

			@Override
			public int getStatusCode() {
				return status.value();
			}

			@Override
			public String getReasonPhrase() {
				return (status instanceof HttpStatus httpStatus) ?  httpStatus.getReasonPhrase() : ex.getMessage();
			}
		}).with(ApiConstants.ErrorKey.MESSAGE, ex.getResponseBodyAsString()).build();

		return create(ex, problem, request);
	}

	@ExceptionHandler
	public ResponseEntity<Problem> handleInternalServerErrorException(InternalServerErrorException ex,
																	  NativeWebRequest request) {
		return create(ex, request,
				HeaderUtil.createFailureAlert(true, ex.getReasonCode(), ex.getErrorKey(), ex.getMessage()));
	}

	@ExceptionHandler
	public ResponseEntity<Problem> handleConcurrencyFailure(ConcurrencyFailureException ex, NativeWebRequest request) {
		Problem problem = Problem.builder().withStatus(Status.CONFLICT)
				.with(ApiConstants.ErrorKey.MESSAGE, Labels.getLabels(LabelKey.ERROR_CONCURRENCY_FAILURE)).build();

		return create(ex, problem, request);
	}

	@ExceptionHandler
	public ResponseEntity<Problem> handleUnauthorizedException(UnauthorizedException ex, NativeWebRequest request) {
		Problem problem = Problem.builder().withStatus(Status.UNAUTHORIZED)
				.with(ApiConstants.ErrorKey.MESSAGE, ex.getMessage())
				.with(ApiConstants.ErrorKey.REASON_CODE, ErrorCode.MSG1032.name())
				.build();

		return create(ex, problem, request);
	}

	@ExceptionHandler(IOException.class)
	public ResponseEntity<Problem> handleIOException(IOException ex, NativeWebRequest request) {
	    if (StringUtils.containsIgnoreCase(ExceptionUtils.getRootCauseMessage(ex), "Broken pipe")) {   //(2)
	        return null;        //(2)	socket is closed, cannot return any response    
	    }
		
		Problem problem = Problem.builder().withStatus(Status.INTERNAL_SERVER_ERROR)
				.with(ApiConstants.ErrorKey.REASON_CODE, ErrorCode.MSG1010.name())
				.with(ApiConstants.ErrorKey.MESSAGE, Labels.getLabels(LabelKey.ERROR_AN_UNEXPECTED_ERROR_HAS_OCCURRED)).build();

		return create(ex, problem, request);
	}

	@ExceptionHandler(ClientResponseException.class)
	public ResponseEntity<Problem> handleClientResponseException(ClientResponseException ex, NativeWebRequest request) {
		Problem problem = Problem.builder().withStatus(Status.BAD_REQUEST)
				.with(ApiConstants.ErrorKey.MESSAGE,ex.getMessage())
//				.with(ApiConstants.ErrorKey.REASON_CODE, ex.getReasonCode())
				.with(ApiConstants.ErrorKey.DATA, null)
				.build();

		return create(ex, problem, request);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Problem> handleException(Exception ex, NativeWebRequest request) {
		Problem problem = Problem.builder().withStatus(Status.INTERNAL_SERVER_ERROR)
				.with(ApiConstants.ErrorKey.MESSAGE, Labels.getLabels(LabelKey.ERROR_AN_UNEXPECTED_ERROR_HAS_OCCURRED)).build();

		return create(ex, problem, request);
	}

	public record FieldError(String objectName, String field, String message) implements Serializable {
		@Serial
		private static final long serialVersionUID = -3173266024911499987L;

	}
}
