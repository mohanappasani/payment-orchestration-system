package com.paymentOrchestration.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.paymentOrchestration.dto.ApiResponse;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

	/**
	 * Handles bean validation exceptions.
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ApiResponse<Object> handleValidationException(MethodArgumentNotValidException ex,
			HttpServletRequest request) {

		Map<String, String> validationErrors = new HashMap<>();

		for (FieldError error : ex.getBindingResult().getFieldErrors()) {
			validationErrors.put(error.getField(), error.getDefaultMessage());
		}

		return ApiResponse.builder().success(false).timestamp(LocalDateTime.now())
				.status(HttpStatus.BAD_REQUEST.value()).message("Validation failed").path(request.getRequestURI())
				.errors(validationErrors).build();
	}

	/**
	 * Handles malformed JSON and invalid enum values.
	 */
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ApiResponse<Object> handleInvalidFormatException(HttpMessageNotReadableException ex,
			HttpServletRequest request) {

		Map<String, String> errors = new HashMap<>();

		errors.put("request", "Malformed JSON request or invalid field value");

		return ApiResponse.builder().success(false).timestamp(LocalDateTime.now())
				.status(HttpStatus.BAD_REQUEST.value()).message("Invalid request payload").path(request.getRequestURI())
				.errors(errors).build();
	}

	/**
	 * Handles all unexpected server exceptions.
	 */
	@ExceptionHandler(Exception.class)
	public ApiResponse<Object> handleGenericException(Exception ex, HttpServletRequest request) {

		Map<String, String> errors = new HashMap<>();

		errors.put("error", ex.getMessage());

		return ApiResponse.builder().success(false).timestamp(LocalDateTime.now())
				.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).message("Internal server error")
				.path(request.getRequestURI()).errors(errors).build();
	}

	/**
	 * Handles payment not found scenarios.
	 */
	@ExceptionHandler(PaymentNotFoundException.class)
	public ApiResponse<Object> handlePaymentNotFoundException(PaymentNotFoundException ex, HttpServletRequest request) {

		Map<String, String> errors = new HashMap<>();

		errors.put("payment", ex.getMessage());

		return ApiResponse.builder().success(false).timestamp(LocalDateTime.now()).status(HttpStatus.NOT_FOUND.value())
				.message("Payment not found").path(request.getRequestURI()).errors(errors).build();
	}

	/**
	 * Handles provider routing failures.
	 */
	@ExceptionHandler(ProviderRoutingException.class)
	public ApiResponse<Object> handleProviderRoutingException(ProviderRoutingException ex, HttpServletRequest request) {

		Map<String, String> errors = new HashMap<>();

		errors.put("routing", ex.getMessage());

		return ApiResponse.builder().success(false).timestamp(LocalDateTime.now())
				.status(HttpStatus.BAD_REQUEST.value()).message("Provider routing failed").path(request.getRequestURI())
				.errors(errors).build();
	}
}