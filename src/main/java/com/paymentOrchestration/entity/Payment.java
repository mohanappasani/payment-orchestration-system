package com.paymentOrchestration.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.paymentOrchestration.enums.PaymentStatus;
import com.paymentOrchestration.enums.PaymentType;
import com.paymentOrchestration.enums.ProviderType;

/**
 * Represents a payment transaction in the orchestration system.
 */
@Entity
@Table(name = "payments")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

	/**
	 * Unique payment identifier.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	/**
	 * Merchant initiating the payment.
	 */
	private String merchantId;

	/**
	 * Payment amount.
	 */
	private BigDecimal amount;

	/**
	 * Transaction currency.
	 */
	private String currency;

	/**
	 * Type of payment method.
	 */
	@Enumerated(EnumType.STRING)
	private PaymentType paymentType;

	/**
	 * Provider used for processing.
	 */
	@Enumerated(EnumType.STRING)
	private ProviderType provider;

	/**
	 * Current payment status.
	 */
	@Enumerated(EnumType.STRING)
	private PaymentStatus status;
	
	@Column(name = "retry_count")
	private Integer retryCount;

	/**
	 * Unique key for idempotent requests.
	 */
	@Column(unique = true)
	private String idempotencyKey;

	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;
}