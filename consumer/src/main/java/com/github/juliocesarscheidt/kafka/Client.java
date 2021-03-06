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

    String elasticsearchHost = System.getenv("ELASTICSEARCH_HOST") != null ?
      System.getenv("ELASTICSEARCH_HOST") : "127.0.0.1";
    Integer elasticsearchPort = System.getenv("ELASTICSEARCH_PORT") != null ?
      Integer.parseInt(System.getenv("ELASTICSEARCH_PORT")) : 9200;

    try {
      // producer test
      // Consumer consumer = new Consumer(bootstrapServers, topic, logger);
      // consumer.start();

      ElasticsearchConsumer elasticsearchConsumer = new ElasticsearchConsumer(bootstrapServers, topic, logger, elasticsearchHost, elasticsearchPort);
      elasticsearchConsumer.start();

    } catch (Exception e) {
      logger.error("Error caught " + e.getMessage());
    }
  }
}
