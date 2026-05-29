package com.paymentOrchestration.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class FetchPaymentResponse {

	/**
	 * Unique payment identifier.
	 */
	private UUID paymentId;

	/**
	 * Merchant identifier.
	 */
	private String merchantId;

	/**
	 * Transaction amount.
	 */
	private BigDecimal amount;

	/**
	 * Currency code.
	 */
	private String currency;

	/**
	 * Payment status.
	 */
	private String status;

	/**
	 * Routed payment provider.
	 */
	private String provider;

	/**
	 * Payment creation timestamp.
	 */
	private LocalDateTime createdAt;
}
