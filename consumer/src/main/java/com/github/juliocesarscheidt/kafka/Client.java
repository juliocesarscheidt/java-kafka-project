package com.github.juliocesarscheidt.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Client {
  public static void main(String[] args) {
    final Logger logger = LoggerFactory.getLogger(Client.class);

    String bootstrapServers = System.getenv("BOOTSTRAP_SERVERS") != null ?
      System.getenv("BOOTSTRAP_SERVERS") :
      "localhost:9092";

    String topic = System.getenv("TOPIC_NAME") != null ?
      System.getenv("TOPIC_NAME") :
      "topic_0";

    try {
      // producer test
      Consumer consumer = new Consumer(bootstrapServers, topic, logger);
      consumer.call();

    } catch (Exception e) {
      logger.error("Error caught " + e.getMessage());
    }
  }
}
