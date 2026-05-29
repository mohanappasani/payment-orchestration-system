package com.paymentOrchestration.service;

import com.paymentOrchestration.dto.CreatePaymentRequest;
import com.paymentOrchestration.dto.FetchPaymentResponse;
import com.paymentOrchestration.dto.PaymentResponse;

import java.util.UUID;

public interface PaymentService {

	/**
	 * Creates a new payment.
	 *
	 * @param request        payment request
	 * @param idempotencyKey unique request key
	 * @return created payment response
	 */
	PaymentResponse createPayment(CreatePaymentRequest request, String idempotencyKey);

	/**
	 * Fetches payment details by payment ID.
	 *
	 * @param paymentId unique payment identifier
	 * @return payment details
	 */
	FetchPaymentResponse getPaymentById(UUID paymentId);
}
