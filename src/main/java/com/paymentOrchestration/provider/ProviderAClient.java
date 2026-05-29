package com.paymentOrchestration.provider;

import org.springframework.stereotype.Component;

import com.paymentOrchestration.dto.CreatePaymentRequest;
import com.paymentOrchestration.dto.ProviderResponse;
import com.paymentOrchestration.enums.ProviderType;

@Component
public class ProviderAClient implements PaymentProvider {

	/**
	 * Simulates Provider A payment processing.
	 */
	@Override
	public ProviderResponse processPayment(CreatePaymentRequest request) {

		return ProviderResponse.builder().success(true).provider(ProviderType.PROVIDER_A.name())
				.message("Payment processed successfully via Provider A").build();
	}

	@Override
	public ProviderType getProviderType() {
		return ProviderType.PROVIDER_A;
	}
}
