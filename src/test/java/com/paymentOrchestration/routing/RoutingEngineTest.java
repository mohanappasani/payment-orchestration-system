package com.paymentOrchestration.routing;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.paymentOrchestration.config.RoutingProperties;
import com.paymentOrchestration.enums.PaymentType;
import com.paymentOrchestration.enums.ProviderType;
import com.paymentOrchestration.exception.ProviderRoutingException;

class RoutingEngineTest {

	private RoutingProperties routingProperties;

	private RoutingEngine routingEngine;

	@BeforeEach
	void setup() {

		routingProperties = new RoutingProperties();

		routingProperties.setRouting(Map.of(PaymentType.CARD, ProviderType.PROVIDER_A,

				PaymentType.UPI, ProviderType.PROVIDER_B));

		routingProperties.setPaymentTypes(List.of("CARD", "UPI"));

		routingEngine = new RoutingEngine(routingProperties);
	}

	/**
	 * Verifies that CARD payments are routed to Provider A.
	 */
	@Test
	void routeCardToProviderA() {

		ProviderType provider = routingEngine.route(PaymentType.CARD);

		assertEquals(ProviderType.PROVIDER_A, provider);
	}

	/**
	 * Verifies that UPI payments are routed to Provider B.
	 */
	@Test
	void routeUpiToProviderB() {

		ProviderType provider = routingEngine.route(PaymentType.UPI);

		assertEquals(ProviderType.PROVIDER_B, provider);
	}

	/**
	 * Verifies that an exception is thrown for unsupported payment types.
	 */
	@Test
	void throwExceptionWhenPaymentTypeNotSupported() {

		routingProperties.setPaymentTypes(List.of("CARD"));

		assertThrows(ProviderRoutingException.class, () -> routingEngine.route(PaymentType.UPI));
	}

	/**
	 * Verifies that the configured fallback provider is returned correctly.
	 */
	@Test
	void getFallbackProvider() {

		routingProperties.setFailover(Map.of(ProviderType.PROVIDER_A, ProviderType.PROVIDER_B));

		ProviderType provider = routingEngine.getFallbackProvider(ProviderType.PROVIDER_A);

		assertEquals(ProviderType.PROVIDER_B, provider);
	}

}
