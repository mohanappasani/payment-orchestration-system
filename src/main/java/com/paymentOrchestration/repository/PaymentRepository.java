package com.paymentOrchestration.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.paymentOrchestration.entity.Payment;

/**
 * Repository for managing payment persistence operations.
 */
@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID> {

	/**
	 * Finds a payment using idempotency key.
	 *
	 * @param idempotencyKey unique idempotency key
	 * @return matching payment if exists
	 */
	Optional<Payment> findByIdempotencyKey(String idempotencyKey);
}