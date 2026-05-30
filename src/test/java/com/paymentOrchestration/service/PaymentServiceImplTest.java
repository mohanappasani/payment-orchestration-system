package com.paymentOrchestration.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.paymentOrchestration.config.RetryProperties;
import com.paymentOrchestration.dto.CreatePaymentRequest;
import com.paymentOrchestration.dto.PaymentResponse;
import com.paymentOrchestration.dto.ProviderResponse;
import com.paymentOrchestration.entity.Payment;
import com.paymentOrchestration.enums.PaymentStatus;
import com.paymentOrchestration.enums.PaymentType;
import com.paymentOrchestration.enums.ProviderType;
import com.paymentOrchestration.provider.PaymentProvider;
import com.paymentOrchestration.repository.PaymentRepository;
import com.paymentOrchestration.routing.ProviderFactory;
import com.paymentOrchestration.routing.RoutingEngine;
import com.paymentOrchestration.service.IdempotencyService;
import com.paymentOrchestration.service.Impl.PaymentServiceImpl;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

	@Mock
	private PaymentRepository paymentRepository;

	@Mock
	private RoutingEngine routingEngine;

	@Mock
	private ProviderFactory providerFactory;

	@Mock
	private IdempotencyService idempotencyService;

	@InjectMocks
	private PaymentServiceImpl paymentService;

	@Mock
	private RetryProperties retryProperties;

	private CreatePaymentRequest request;

	@BeforeEach
	void setup() {

		request = new CreatePaymentRequest();

		request.setMerchantId("M001");
		request.setAmount(BigDecimal.valueOf(1000));
		request.setCurrency("INR");
		request.setPaymentType(PaymentType.CARD);
	}

	/**
	 * Verifies successful payment creation and processing through the configured
	 * provider.
	 */
	@Test
	void createPayment_success() {

		String idempotencyKey = "test-key";

		when(idempotencyService.getCachedResponse(idempotencyKey)).thenReturn(null);

		when(routingEngine.route(PaymentType.CARD)).thenReturn(ProviderType.PROVIDER_A);

		when(retryProperties.getMaxAttempts()).thenReturn(2);

		PaymentProvider provider = mock(PaymentProvider.class);

		when(providerFactory.getProvider(ProviderType.PROVIDER_A)).thenReturn(provider);

		when(provider.processPayment(any()))
				.thenReturn(ProviderResponse.builder().success(true).message("Success").build());

		PaymentResponse response = paymentService.createPayment(request, idempotencyKey);

		assertNotNull(response);

		assertEquals("SUCCESS", response.getStatus());

		verify(paymentRepository, atLeastOnce()).save(any(Payment.class));
	}

	/**
	 * Verifies that an existing idempotency key returns the cached response without
	 * reprocessing.
	 */
	@Test
	void createPayment_returnsCachedResponse() {

		String idempotencyKey = "existing-key";

		PaymentResponse cached = PaymentResponse.builder().status("SUCCESS").build();

		when(idempotencyService.getCachedResponse(idempotencyKey)).thenReturn(cached);

		PaymentResponse response = paymentService.createPayment(request, idempotencyKey);

		assertEquals(cached, response);

		verify(paymentRepository, never()).save(any());
	}

	/**
	 * Verifies successful retrieval of payment details using a valid payment ID.
	 */
	@Test
	void getPaymentById_success() {

		UUID paymentId = UUID.randomUUID();

		Payment payment = Payment.builder().id(paymentId).merchantId("M001").status(PaymentStatus.SUCCESS).build();

		when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(payment));

		assertNotNull(paymentService.getPaymentById(paymentId));
	}

	/**
	 * Verifies that an exception is thrown when a payment is not found.
	 */
	@Test
	void getPaymentById_notFound() {

		UUID paymentId = UUID.randomUUID();

		when(paymentRepository.findById(paymentId)).thenReturn(Optional.empty());

		assertThrows(RuntimeException.class, () -> paymentService.getPaymentById(paymentId));
	}
}
