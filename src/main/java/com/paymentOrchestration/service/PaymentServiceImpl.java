package com.paymentOrchestration.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.paymentOrchestration.dto.CreatePaymentRequest;
import com.paymentOrchestration.dto.FetchPaymentResponse;
import com.paymentOrchestration.dto.PaymentResponse;
import com.paymentOrchestration.entity.Payment;
import com.paymentOrchestration.enums.PaymentStatus;
import com.paymentOrchestration.exception.PaymentNotFoundException;
import com.paymentOrchestration.repository.PaymentRepository;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

	private final PaymentRepository paymentRepository;

	/**
	 * Creates and stores a new payment request.
	 *
	 * @param request        payment request payload
	 * @param idempotencyKey unique idempotency key
	 * @return payment creation response
	 */
	@Override
	public PaymentResponse createPayment(CreatePaymentRequest request, String idempotencyKey) {

		Payment payment = Payment.builder().merchantId(request.getMerchantId()).amount(request.getAmount())
				.currency(request.getCurrency()).paymentType(request.getPaymentType()).status(PaymentStatus.PENDING)
				.idempotencyKey(idempotencyKey).createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();

		paymentRepository.save(payment);

		return PaymentResponse.builder().paymentId(payment.getId()).status(payment.getStatus().name())
				.provider(payment.getProvider() != null ? payment.getProvider().name() : null)
				.message("Payment created successfully").build();
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