package com.paymentOrchestration.routing;

import org.springframework.stereotype.Component;

import com.paymentOrchestration.config.RoutingProperties;
import com.paymentOrchestration.enums.PaymentType;
import com.paymentOrchestration.enums.ProviderType;
import com.paymentOrchestration.exception.ProviderRoutingException;

import lombok.RequiredArgsConstructor;

/**
 * Responsible for routing payments to configured providers.
 */
@Component
@RequiredArgsConstructor
public class RoutingEngine {

	private final RoutingProperties routingProperties;

	/**
	 * Resolves provider dynamically from configuration.
	 *
	 * @param paymentType payment method type
	 * @return configured provider
	 */
	public ProviderType route(PaymentType paymentType) {

		if (routingProperties.getRouting() == null) {
			throw new ProviderRoutingException("Payment routing configuration not loaded. Check configurations");
		}

		if (!routingProperties.getPaymentTypes().contains(paymentType.name())) {
			throw new ProviderRoutingException("Payment type not supported: " + paymentType + ". Supported types: " + routingProperties.getPaymentTypes());
		}

		ProviderType provider = routingProperties.getRouting().get(paymentType);

		if (provider == null) {
			throw new ProviderRoutingException("No provider configured for payment type: " + paymentType);
		}

		return provider;
	}
}