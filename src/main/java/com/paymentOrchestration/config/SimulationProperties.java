package com.paymentOrchestration.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Holds provider simulation configuration.
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "payment.simulation")
public class SimulationProperties {

	/**
	 * Enable Provider A failure simulation.
	 */
	private boolean providerAFailureEnabled;
}
