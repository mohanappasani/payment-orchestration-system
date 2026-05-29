package com.paymentOrchestration.provider;

import com.paymentOrchestration.dto.CreatePaymentRequest;
import com.paymentOrchestration.dto.ProviderResponse;
import com.paymentOrchestration.enums.ProviderType;

/**
 * Contract for payment provider integrations.
 */
public interface PaymentProvider {

    /**
     * Processes payment request through provider.
     *
     * @param request payment request
     * @return provider processing response
     */
    ProviderResponse processPayment(CreatePaymentRequest request);

    /**
     * Returns provider type.
     *
     * @return provider type
     */
    ProviderType getProviderType();
}
