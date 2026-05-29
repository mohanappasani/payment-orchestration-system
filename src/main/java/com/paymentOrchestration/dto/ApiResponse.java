package com.paymentOrchestration.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Builder
@AllArgsConstructor
public class ApiResponse<T> {

	/**
	 * Indicates whether request processing was successful.
	 */
	private boolean success;

	/**
	 * API response timestamp.
	 */
	private LocalDateTime timestamp;

	/**
	 * HTTP status code.
	 */
	private int status;

	/**
	 * Human-readable response message.
	 */
	private String message;

	/**
	 * API endpoint path.
	 */
	private String path;

	/**
	 * Successful response payload.
	 */
	private T data;

	/**
	 * Error details if request fails.
	 */
	private Map<String, String> errors;
}
