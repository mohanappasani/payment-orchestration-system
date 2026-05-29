package com.paymentOrchestration.service;

import com.paymentOrchestration.dto.CreatePaymentRequest;
import com.paymentOrchestration.dto.PaymentResponse;

public interface PaymentService {

	/**
	 * Creates a new payment in the system.
	 *
	 * @param request        payment creation request
	 * @param idempotencyKey unique request identifier
	 * @return payment response
	 */
	PaymentResponse createPayment(CreatePaymentRequest request, String idempotencyKey);
}
