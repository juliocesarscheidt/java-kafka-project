package com.github.juliocesarscheidt.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Client {
	public static void main(String[] args) {
    final Logger logger = LoggerFactory.getLogger(Producer.class);

    String bootstrapServers = System.getenv("BOOTSTRAP_SERVERS") != null ?
			System.getenv("BOOTSTRAP_SERVERS") :
			"kafka:9092";

		String topic = System.getenv("TOPIC_NAME") != null ?
      System.getenv("TOPIC_NAME") :
      "topic_0";

		// producer test
		Producer.call(bootstrapServers, topic, logger);

		// producer test
		Consumer.call(bootstrapServers, topic, logger);
	}
}
