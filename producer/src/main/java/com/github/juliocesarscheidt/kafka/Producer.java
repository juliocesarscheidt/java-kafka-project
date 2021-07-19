package com.github.juliocesarscheidt.kafka;

import java.util.Properties;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;

public class Producer {
  final private String bootstrapServers;
  final private String topic;
  final private Logger logger;

  public Producer(String bootstrapServers, String topic, final Logger logger) {
    this.bootstrapServers = bootstrapServers;
    this.topic = topic;
    this.logger = logger;
  }

  public KafkaProducer<String, String> createProducer() {
    // create the config
    Properties config = new Properties();

    config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, this.bootstrapServers);
    // to send strings we need a string serializer
    config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
    config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
    config.put(ProducerConfig.ACKS_CONFIG, "all");

    // create the producer
    KafkaProducer<String, String> producer = new KafkaProducer<String, String>(config);

    return producer;
  }

  public void sendMessage(KafkaProducer<String, String> producer, String topic, String key, String message, final Logger logger) {
    // send a message to topic, asynchronously
    ProducerRecord<String, String> record = new ProducerRecord<String, String>(topic, key, message);

    producer.send(record, new Callback() {
      public void onCompletion(RecordMetadata metadata, Exception exception) {
        if (exception != null) {
          logger.error("Send failed for record " + exception);
        } else {
          logger.info("[INFO] metadata offset " + metadata.offset());
          logger.info("[INFO] metadata partition " + metadata.partition());
          logger.info("[INFO] metadata topic " + metadata.topic());
          logger.info("[INFO] metadata timestamp " + metadata.timestamp());
        }
      }
    });
  }

  public void start() {
    // create the producer
    KafkaProducer<String, String> producer = this.createProducer();

    String message = "Hello World";

    // send data to a topic
    for (int i = 0; i < 5; i ++) {
      // the partition will be chosen according to the key, same key = same partition
      String key = "id_" + i;
      String value = message + " :: " + i;

      this.sendMessage(producer, this.topic, key, value, this.logger);
    }

    // flush data
    producer.flush();

    // flush and close
    producer.close();
  }
}
