package com.github.juliocesarscheidt.kafka;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;

public class Consumer {
  final private String bootstrapServers;
  final private String topic;
  final private Logger logger;

  public Consumer(String bootstrapServers, String topic, final Logger logger) {
    this.bootstrapServers = bootstrapServers;
    this.topic = topic;
    this.logger = logger;
  }

  public KafkaConsumer<String, String> getConsumer(Properties config) {
    // create the consumer
    KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(config);

    return consumer;
  }

  public void start() {
    // create the config
    Properties config = new Properties();

    config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, this.bootstrapServers);
    // to receive strings we need a string deserializer
    config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
    config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
    // the consumer group id
    String groupID = "example_group_id";
    config.put(ConsumerConfig.GROUP_ID_CONFIG, groupID);
    // earliest, latest, none
    config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

    // create consumer
    KafkaConsumer<String, String> consumer = getConsumer(config);

    // subscribe the consumer on topics
    consumer.subscribe(Arrays.asList(this.topic));

    while (true) {
      ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000)); // 1000 milliseconds

      for (ConsumerRecord<String, String> record: records) {
        this.logger.info("[INFO] record key " + record.key());
        this.logger.info("[INFO] record value " + record.value());
        this.logger.info("[INFO] record partition " + record.partition());
        this.logger.info("[INFO] record offset " + record.offset());
      }
    }
  }
}
