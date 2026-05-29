package com.paymentOrchestration.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ProviderResponse {

    /**
     * Indicates whether provider processing succeeded.
     */
    private boolean success;

    /**
     * Provider name.
     */
    private String provider;

    /**
     * Provider response message.
     */
    private String message;
}