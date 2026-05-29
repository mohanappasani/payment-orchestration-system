package com.paymentOrchestration.service.Impl;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.paymentOrchestration.dto.CreatePaymentRequest;
import com.paymentOrchestration.dto.FetchPaymentResponse;
import com.paymentOrchestration.dto.PaymentResponse;
import com.paymentOrchestration.dto.ProviderResponse;
import com.paymentOrchestration.entity.Payment;
import com.paymentOrchestration.enums.PaymentStatus;
import com.paymentOrchestration.enums.ProviderType;
import com.paymentOrchestration.exception.PaymentNotFoundException;
import com.paymentOrchestration.provider.PaymentProvider;
import com.paymentOrchestration.repository.PaymentRepository;
import com.paymentOrchestration.routing.ProviderFactory;
import com.paymentOrchestration.routing.RoutingEngine;
import com.paymentOrchestration.service.IdempotencyService;
import com.paymentOrchestration.service.PaymentService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

	private final PaymentRepository paymentRepository;

	private final RoutingEngine routingEngine;

	private final ProviderFactory providerFactory;

	private final IdempotencyService idempotencyService;

	/**
	 * Creates and processes a payment request.
	 *
	 * Flow: - Persist payment - Route payment - Resolve provider - Process payment
	 * - Update payment status
	 *
	 * @param request        payment request
	 * @param idempotencyKey unique request identifier
	 * @return payment response
	 */
	@Override
	public PaymentResponse createPayment(CreatePaymentRequest request, String idempotencyKey) {

		// Check existing cached response
		PaymentResponse cachedResponse = idempotencyService.getCachedResponse(idempotencyKey);

		if (cachedResponse != null) {
			return cachedResponse;
		}

		Payment payment = Payment.builder().merchantId(request.getMerchantId()).amount(request.getAmount())
				.currency(request.getCurrency()).paymentType(request.getPaymentType()).status(PaymentStatus.PROCESSING)
				.idempotencyKey(idempotencyKey).createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();

		paymentRepository.save(payment);

		// Route payment
		ProviderType providerType = routingEngine.route(request.getPaymentType());

		// Resolve provider
		PaymentProvider provider = providerFactory.getProvider(providerType);

		// Process payment
		ProviderResponse providerResponse = provider.processPayment(request);

		// Update payment
		payment.setProvider(providerType);

		if (providerResponse.isSuccess()) {

			payment.setStatus(PaymentStatus.SUCCESS);

		} else {

			payment.setStatus(PaymentStatus.FAILED);
		}

		payment.setUpdatedAt(LocalDateTime.now());

		paymentRepository.save(payment);

		PaymentResponse paymentResponse = PaymentResponse.builder().paymentId(payment.getId())
				.status(payment.getStatus().name()).provider(providerType.name()).message(providerResponse.getMessage())
				.build();

		idempotencyService.cacheResponse(idempotencyKey, paymentResponse);

		return paymentResponse;
	}

	/**
	 * Retrieves payment details using payment ID.
	 *
	 * @param paymentId payment identifier
	 * @return payment details
	 */
	@Override
	public FetchPaymentResponse getPaymentById(UUID paymentId) {

		Payment payment = paymentRepository.findById(paymentId)
				.orElseThrow(() -> new PaymentNotFoundException("Payment not found for ID: " + paymentId));

		return FetchPaymentResponse.builder().paymentId(payment.getId()).merchantId(payment.getMerchantId())
				.amount(payment.getAmount()).currency(payment.getCurrency()).status(payment.getStatus().name())
				.provider(payment.getProvider() != null ? payment.getProvider().name() : null)
				.createdAt(payment.getCreatedAt()).build();
	}
}