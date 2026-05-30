package com.paymentOrchestration.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.paymentOrchestration.dto.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

	/**
	 * Handles bean validation failures.
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiResponse<Object>> handleValidationException(MethodArgumentNotValidException ex,
			HttpServletRequest request) {

		Map<String, String> errors = new HashMap<>();

		ex.getBindingResult().getFieldErrors()
				.forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

		ApiResponse<Object> response = ApiResponse.builder().success(false).timestamp(LocalDateTime.now())
				.status(HttpStatus.BAD_REQUEST.value()).message("Validation failed").path(request.getRequestURI())
				.errors(errors).build();

		return ResponseEntity.badRequest().body(response);
	}

	/**
	 * Handles malformed JSON and invalid enum values.
	 */
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ApiResponse<Object>> handleInvalidFormatException(HttpMessageNotReadableException ex,
			HttpServletRequest request) {

		Map<String, String> errors = new HashMap<>();

		errors.put("request", "Malformed JSON request or invalid field value");

		ApiResponse<Object> response = ApiResponse.builder().success(false).timestamp(LocalDateTime.now())
				.status(HttpStatus.BAD_REQUEST.value()).message("Invalid request payload").path(request.getRequestURI())
				.errors(errors).build();

		return ResponseEntity.badRequest().body(response);
	}

	/**
	 * Handles payment not found scenarios.
	 */
	@ExceptionHandler(PaymentNotFoundException.class)
	public ResponseEntity<ApiResponse<Object>> handlePaymentNotFoundException(PaymentNotFoundException ex,
			HttpServletRequest request) {

		Map<String, String> errors = new HashMap<>();

		errors.put("payment", ex.getMessage());

		ApiResponse<Object> response = ApiResponse.builder().success(false).timestamp(LocalDateTime.now())
				.status(HttpStatus.NOT_FOUND.value()).message("Payment not found").path(request.getRequestURI())
				.errors(errors).build();

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	}

	/**
	 * Handles provider routing failures.
	 */
	@ExceptionHandler(ProviderRoutingException.class)
	public ResponseEntity<ApiResponse<Object>> handleProviderRoutingException(ProviderRoutingException ex,
			HttpServletRequest request) {

		Map<String, String> errors = new HashMap<>();

		errors.put("routing", ex.getMessage());

		ApiResponse<Object> response = ApiResponse.builder().success(false).timestamp(LocalDateTime.now())
				.status(HttpStatus.BAD_REQUEST.value()).message("Provider routing failed").path(request.getRequestURI())
				.errors(errors).build();

		return ResponseEntity.badRequest().body(response);
	}

	/**
	 * Handles all unexpected server exceptions.
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiResponse<Object>> handleGenericException(Exception ex, HttpServletRequest request) {

		Map<String, String> errors = new HashMap<>();

		errors.put("error", ex.getMessage());

		ApiResponse<Object> response = ApiResponse.builder().success(false).timestamp(LocalDateTime.now())
				.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).message("Internal server error")
				.path(request.getRequestURI()).errors(errors).build();

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	}

}
