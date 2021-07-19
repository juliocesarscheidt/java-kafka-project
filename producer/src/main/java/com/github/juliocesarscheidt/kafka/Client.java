package com.github.juliocesarscheidt.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Client {
  public static void main(String[] args) {
    final Logger logger = LoggerFactory.getLogger(Client.class);

    String bootstrapServers = System.getenv("BOOTSTRAP_SERVERS") != null ?
      System.getenv("BOOTSTRAP_SERVERS") : "127.0.0.1:9092";

    String topic = System.getenv("TOPIC_NAME") != null ?
      System.getenv("TOPIC_NAME") : "topic_0";

    try {
      // producer
      Producer producer = new Producer(bootstrapServers, topic, logger);
      producer.start();

      // twitter producer
      TwitterProducer twitterProducer = new TwitterProducer(bootstrapServers, topic, logger);
      twitterProducer.start();

    } catch (Exception e) {
      logger.error("Error caught " + e.getMessage());
    }
  }
}
