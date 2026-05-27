package paymentOrchestration;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class PaymentOrchestrationApplicationTests {

	@Test
	void contextLoads() {
	}

}
