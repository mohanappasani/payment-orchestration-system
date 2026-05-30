package com.paymentOrchestration.provider;

import org.springframework.stereotype.Component;

import com.paymentOrchestration.config.SimulationProperties;
import com.paymentOrchestration.dto.CreatePaymentRequest;
import com.paymentOrchestration.dto.ProviderResponse;
import com.paymentOrchestration.enums.ProviderType;

import lombok.RequiredArgsConstructor;

/**
 * Provider A payment processor.
 */
@Component
@RequiredArgsConstructor
public class ProviderAClient implements PaymentProvider {

	private final SimulationProperties simulationProperties;

	/**
	 * Processes payment through Provider A.
	 */
	@Override
	public ProviderResponse processPayment(CreatePaymentRequest request) {

		// Simulate provider failure
		if (simulationProperties.isProviderAFailureEnabled()) {

			return ProviderResponse.builder().success(false).provider(ProviderType.PROVIDER_A.name())
					.message("Provider A simulated failure").build();
		}

		return ProviderResponse.builder().success(true).provider(ProviderType.PROVIDER_A.name())
				.message("Payment processed successfully via Provider A").build();
	}

	@Override
	public ProviderType getProviderType() {
		return ProviderType.PROVIDER_A;
	}
}
