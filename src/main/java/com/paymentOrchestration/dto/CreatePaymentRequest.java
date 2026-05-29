package com.paymentOrchestration.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

import com.paymentOrchestration.enums.PaymentType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatePaymentRequest {

	/**
	 * Unique merchant identifier. Example: MERCHANT_001
	 */
	@NotBlank(message = "Merchant ID is required")
	@Pattern(regexp = "^[A-Za-z0-9_-]{3,50}$", message = "Merchant ID must contain only letters, numbers, underscore or hyphen")
	private String merchantId;

	/**
	 * Transaction amount.
	 */
	@NotNull(message = "Amount is required")
	@DecimalMin(value = "1.00", inclusive = true, message = "Amount must be greater than or equal to 1")
	@Digits(integer = 10, fraction = 2, message = "Amount format is invalid")
	private BigDecimal amount;

	/**
	 * ISO currency code. Example: INR, USD
	 */
	@NotBlank(message = "Currency is required")
	@Pattern(regexp = "^[A-Z]{3}$", message = "Currency must be a valid 3-letter uppercase ISO code")
	private String currency;

	/**
	 * Payment method type.
	 */
	@NotNull(message = "Payment type is required")
	private PaymentType paymentType;
}