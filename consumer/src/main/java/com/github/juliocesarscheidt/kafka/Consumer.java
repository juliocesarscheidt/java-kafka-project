package com.github.juliocesarscheidt.kafka;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;

import com.google.gson.JsonParser;

public class Consumer {
  protected String bootstrapServers;
  protected String topic;
  protected Logger logger;
  protected KafkaConsumer<String, String> consumer;

  public String extractValueFromJson(String jsonString, String field) {
    JsonParser parser = new JsonParser();

    return parser.parse(jsonString)
      .getAsJsonObject()
      .get(field)
      .getAsString();
  }

  public Consumer(String bootstrapServers, String topic, final Logger logger) {
    this.bootstrapServers = bootstrapServers;
    this.topic = topic;
    this.logger = logger;

	// create the consumer
    this.consumer = createConsumer();
  }

  public KafkaConsumer<String, String> createConsumer() {
    // create the config
    Properties config = new Properties();

    config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, this.bootstrapServers);

    // to receive strings we need a string deserializer
    config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
    config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

    // the consumer group id
    String groupID = "kafka-elasticsearch-group";
    config.put(ConsumerConfig.GROUP_ID_CONFIG, groupID);

    // earliest, latest, none
    config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

    // auto commit
    config.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
    
    // max poll records
    config.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 100);

    // create the consumer
    KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(config);

    return consumer;
  }

  public void start() {
    int minBatchSize = 100; // a few messages to try out
    List<ConsumerRecord<String, String>> buffer = new ArrayList<>();

    // subscribe the consumer on topics
    this.consumer.subscribe(Arrays.asList(this.topic));

    while (true) {
      ConsumerRecords<String, String> records = this.consumer.poll(Duration.ofMillis(5000)); // 5000 milliseconds

      for (ConsumerRecord<String, String> record: records) {
        buffer.add(record);

        this.logger.info("[INFO] record key " + record.key());
        this.logger.info("[INFO] record value " + record.value());
        this.logger.info("[INFO] record partition " + record.partition());
        this.logger.info("[INFO] record offset " + record.offset());
      }

      if (buffer.size() >= minBatchSize) {
        this.logger.info("[INFO] committing messages");

        // commit the offset
        this.consumer.commitSync();
        buffer.clear();
      }
    }
  }
}
