package com.paymentOrchestration.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paymentOrchestration.dto.PaymentResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

	/**
	 * Redis template configuration for payment response caching.
	 */
	@Bean
	public RedisTemplate<String, PaymentResponse> redisTemplate(RedisConnectionFactory connectionFactory,
			ObjectMapper objectMapper) {

		RedisTemplate<String, PaymentResponse> template = new RedisTemplate<>();

		template.setConnectionFactory(connectionFactory);

		Jackson2JsonRedisSerializer<PaymentResponse> serializer = new Jackson2JsonRedisSerializer<>(objectMapper,
				PaymentResponse.class);

		template.setKeySerializer(new StringRedisSerializer());

		template.setValueSerializer(serializer);

		template.setHashKeySerializer(new StringRedisSerializer());

		template.setHashValueSerializer(serializer);

		template.afterPropertiesSet();

		return template;
	}
}