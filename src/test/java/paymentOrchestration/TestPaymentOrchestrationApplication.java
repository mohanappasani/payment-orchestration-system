package paymentOrchestration;

import org.springframework.boot.SpringApplication;

import com.paymentOrchestration.PaymentOrchestrationApplication;

public class TestPaymentOrchestrationApplication {

	public static void main(String[] args) {
		SpringApplication.from(PaymentOrchestrationApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
