package com.paymentOrchestration.exception;

/**
 * Thrown when provider routing configuration is missing.
 */
public class ProviderRoutingException
        extends RuntimeException {

    public ProviderRoutingException(String message) {
        super(message);
    }
}
