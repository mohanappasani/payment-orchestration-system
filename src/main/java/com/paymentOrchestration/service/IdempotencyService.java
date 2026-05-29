package com.paymentOrchestration.service;

import com.paymentOrchestration.dto.PaymentResponse;

/**
 * Handles idempotency operations using Redis.
 */
public interface IdempotencyService {

	/**
	 * Retrieves cached payment response.
	 *
	 * @param idempotencyKey unique request key
	 * @return cached payment response
	 */
	PaymentResponse getCachedResponse(String idempotencyKey);

	/**
	 * Stores payment response in Redis.
	 *
	 * @param idempotencyKey unique request key
	 * @param response       payment response
	 */
	void cacheResponse(String idempotencyKey, PaymentResponse response);
}