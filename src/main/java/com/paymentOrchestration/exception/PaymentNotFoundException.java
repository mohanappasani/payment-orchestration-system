package com.paymentOrchestration.exception;

/**
 * Thrown when payment is not found in the system.
 */
public class PaymentNotFoundException extends RuntimeException {

	public PaymentNotFoundException(String message) {
		super(message);
	}
}
