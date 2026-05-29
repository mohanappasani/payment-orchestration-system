package com.paymentOrchestration.controller;

import com.paymentOrchestration.dto.ApiResponse;
import com.paymentOrchestration.dto.CreatePaymentRequest;
import com.paymentOrchestration.dto.FetchPaymentResponse;
import com.paymentOrchestration.dto.PaymentResponse;
import com.paymentOrchestration.service.PaymentService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

	private final PaymentService paymentService;

	/**
	 * Creates a new payment request.
	 */
	@PostMapping
	public ResponseEntity<ApiResponse<PaymentResponse>> createPayment(

			@RequestHeader("Idempotency-Key") String idempotencyKey,

			@Valid @RequestBody CreatePaymentRequest request,

			HttpServletRequest httpRequest) {

		PaymentResponse paymentResponse = paymentService.createPayment(request, idempotencyKey);

		ApiResponse<PaymentResponse> response = ApiResponse.<PaymentResponse>builder().success(true)
				.timestamp(LocalDateTime.now()).status(HttpStatus.CREATED.value())
				.message("Payment created successfully").path(httpRequest.getRequestURI()).data(paymentResponse)
				.build();

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	/**
	 * Fetches payment details using payment ID.
	 */
	@GetMapping("/{paymentId}")
	public ResponseEntity<ApiResponse<FetchPaymentResponse>> getPayment(

			@PathVariable UUID paymentId,

			HttpServletRequest httpRequest) {

		FetchPaymentResponse paymentResponse = paymentService.getPaymentById(paymentId);

		ApiResponse<FetchPaymentResponse> response = ApiResponse.<FetchPaymentResponse>builder().success(true)
				.timestamp(LocalDateTime.now()).status(HttpStatus.OK.value()).message("Payment fetched successfully")
				.path(httpRequest.getRequestURI()).data(paymentResponse).build();

		return ResponseEntity.ok(response);
	}
}