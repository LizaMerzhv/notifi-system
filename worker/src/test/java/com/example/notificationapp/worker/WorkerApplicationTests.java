package com.example.notificationapp.worker;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;

@EmbeddedKafka(partitions = 1)
@SpringBootTest(properties = "spring.kafka.bootstrap-servers=${spring.embedded.kafka.brokers}")
class WorkerApplicationTests {

	@Test
	void contextLoads() {
	}

}
