package org.pauldeen.playground;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;

import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.kafka.KafkaContainer;
import org.testcontainers.postgresql.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.junit.jupiter.Container;

@SpringBootTest
@Testcontainers
class PlaygroundApplicationTests {

    // 1. PostgreSQL Container Setup
	@Container
	@ServiceConnection
	static PostgreSQLContainer postgres =
		new PostgreSQLContainer("postgres:18-alpine");

    // 2. Kafka Container Setup
	@Container
	@ServiceConnection
	static KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("apache/kafka:4.3.1"));

	@Test
	void contextLoads() {
	}

}
