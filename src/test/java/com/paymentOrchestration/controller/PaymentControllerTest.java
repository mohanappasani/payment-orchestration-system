package com.paymentOrchestration.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paymentOrchestration.dto.CreatePaymentRequest;
import com.paymentOrchestration.dto.FetchPaymentResponse;
import com.paymentOrchestration.dto.PaymentResponse;
import com.paymentOrchestration.exception.GlobalExceptionHandler;
import com.paymentOrchestration.exception.PaymentNotFoundException;
import com.paymentOrchestration.service.PaymentService;

@WebMvcTest(PaymentController.class)
@Import(GlobalExceptionHandler.class)
class PaymentControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private PaymentService paymentService;

	/**
	 * Verifies successful payment creation request.
	 */
	@Test
	@DisplayName("Should create payment successfully")
	void createPayment_success() throws Exception {

		PaymentResponse response = PaymentResponse.builder().paymentId(UUID.randomUUID()).status("SUCCESS")
				.provider("PROVIDER_A").message("Payment processed").build();

		when(paymentService.createPayment(any(), any())).thenReturn(response);

		CreatePaymentRequest request = new CreatePaymentRequest();

		request.setMerchantId("M001");
		request.setAmount(BigDecimal.valueOf(1000));
		request.setCurrency("INR");
		request.setPaymentType(com.paymentOrchestration.enums.PaymentType.CARD);

		mockMvc.perform(post("/api/v1/payments").header("Idempotency-Key", "test-key").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))).andExpect(status().isCreated());
	}

	/**
	 * Verifies validation failure for invalid request payload.
	 */
	@Test
	@DisplayName("Should return bad request for invalid payload")
	void createPayment_validationFailure() throws Exception {

		String invalidRequest = """
				{
				  "merchantId":"",
				  "amount":-100
				}
				""";

		mockMvc.perform(post("/api/v1/payments").header("Idempotency-Key", "test-key").contentType(MediaType.APPLICATION_JSON)
				.content(invalidRequest)).andExpect(status().isBadRequest());
	}

	/**
	 * Verifies successful retrieval of payment details.
	 */
	@Test
	@DisplayName("Should fetch payment successfully")
	void getPayment_success() throws Exception {

		UUID paymentId = UUID.randomUUID();

		FetchPaymentResponse response = FetchPaymentResponse.builder().paymentId(paymentId).merchantId("M001")
				.amount(BigDecimal.valueOf(1000)).currency("INR").status("SUCCESS").createdAt(LocalDateTime.now())
				.build();

		when(paymentService.getPaymentById(paymentId)).thenReturn(response);

		mockMvc.perform(get("/api/v1/payments/" + paymentId)).andExpect(status().isOk());
	}

	/**
	 * Verifies not found response when payment does not exist.
	 */
	@Test
	@DisplayName("Should return not found when payment does not exist")
	void getPayment_notFound() throws Exception {

		UUID paymentId = UUID.randomUUID();

		when(paymentService.getPaymentById(paymentId)).thenThrow(new PaymentNotFoundException("Payment not found"));

		mockMvc.perform(get("/api/v1/payments/" + paymentId)).andExpect(status().isNotFound());
	}
}
