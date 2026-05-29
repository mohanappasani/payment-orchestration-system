package com.paymentOrchestration.service.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.paymentOrchestration.dto.PaymentResponse;
import com.paymentOrchestration.service.IdempotencyService;

import java.time.Duration;

/**
 * Redis implementation for idempotency management.
 */
@Service
@RequiredArgsConstructor
public class IdempotencyServiceImpl implements IdempotencyService {

	private static final String KEY_PREFIX = "payment:idempotency:";

	private static final Duration TTL = Duration.ofHours(24);

	private final RedisTemplate<String, PaymentResponse> redisTemplate;

	/**
	 * Fetches cached response from Redis.
	 */
	@Override
	public PaymentResponse getCachedResponse(String idempotencyKey) {

		return redisTemplate.opsForValue().get(buildKey(idempotencyKey));
	}

	/**
	 * Stores response in Redis.
	 */
	@Override
	public void cacheResponse(String idempotencyKey, PaymentResponse response) {

		redisTemplate.opsForValue().set(buildKey(idempotencyKey), response, TTL);
	}

	/**
	 * Builds Redis key.
	 */
	private String buildKey(String idempotencyKey) {

		return KEY_PREFIX + idempotencyKey;
	}
}
