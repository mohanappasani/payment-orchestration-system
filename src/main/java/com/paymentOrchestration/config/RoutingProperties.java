package com.paymentOrchestration.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.paymentOrchestration.enums.PaymentType;
import com.paymentOrchestration.enums.ProviderType;

import java.util.List;
import java.util.Map;

/**
 * Holds provider routing configuration.
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "payment")
public class RoutingProperties {

    /**
     * Payment type to provider mapping.
     */
    private Map<PaymentType, ProviderType> routing;

    /**
     * List of supported payment types as strings.
     */
    private List<String> paymentTypes;

    /**
     * List of supported provider types as strings.
     */
    private List<String> providerTypes;

    
    private Map<ProviderType, ProviderType> failover;
}
