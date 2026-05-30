package com.paymentOrchestration.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

/**
 * Retry configuration properties.
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "payment.retry")
public class RetryProperties {

    /**
     * Maximum retry attempts.
     */
    private int maxAttempts;
}
