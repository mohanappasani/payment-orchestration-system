package com.paymentOrchestration.routing;

import org.springframework.stereotype.Component;

import com.paymentOrchestration.enums.ProviderType;
import com.paymentOrchestration.provider.PaymentProvider;

import java.util.List;

/**
 * Factory responsible for resolving provider implementations dynamically.
 */
@Component
public class ProviderFactory {

	private final List<PaymentProvider> providers;

	public ProviderFactory(List<PaymentProvider> providers) {
		this.providers = providers;
	}

	/**
	 * Returns matching provider implementation.
	 *
	 * @param providerType provider identifier
	 * @return provider implementation
	 */
	public PaymentProvider getProvider(ProviderType providerType) {

		return providers.stream().filter(provider -> provider.getProviderType() == providerType).findFirst()
				.orElseThrow(() -> new RuntimeException("Provider not found: " + providerType));
	}
}
