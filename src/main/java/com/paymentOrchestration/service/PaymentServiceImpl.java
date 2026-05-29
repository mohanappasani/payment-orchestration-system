package com.paymentOrchestration.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.paymentOrchestration.dto.CreatePaymentRequest;
import com.paymentOrchestration.dto.PaymentResponse;
import com.paymentOrchestration.entity.Payment;
import com.paymentOrchestration.enums.PaymentStatus;
import com.paymentOrchestration.repository.PaymentRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

	private final PaymentRepository paymentRepository;

	/**
	 * Creates and persists a payment with initial PENDING status.
	 *
	 * @param request        payment request payload
	 * @param idempotencyKey unique request identifier
	 * @return created payment response
	 */
	@Override
	public PaymentResponse createPayment(CreatePaymentRequest request, String idempotencyKey) {

		Payment payment = Payment.builder().merchantId(request.getMerchantId()).amount(request.getAmount())
				.currency(request.getCurrency()).paymentType(request.getPaymentType()).status(PaymentStatus.PENDING)
				.idempotencyKey(idempotencyKey).createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();

		paymentRepository.save(payment);

		return PaymentResponse.builder().paymentId(payment.getId()).status(payment.getStatus().name())
				.message("Payment created successfully").build();
	}
}
