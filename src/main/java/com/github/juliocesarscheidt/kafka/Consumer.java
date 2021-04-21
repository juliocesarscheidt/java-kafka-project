package com.github.juliocesarscheidt.kafka;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
// import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;

public class Consumer {
  public static KafkaConsumer<String, String> getConsumer(Properties config) {
    // create the consumer
    KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(config);

    return consumer;
  }

  public static void call(String bootstrapServers, String topic, final Logger logger) {
    // create the config
    Properties config = new Properties();

    config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
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
    consumer.subscribe(Arrays.asList(topic));

    // assign to a topic/partition
    // TopicPartition partition = new TopicPartition(topic, 0);
    // consumer.assign(Arrays.asList(partition));

    // seek data
    // long offset = 0L;
    // consumer.seek(partition, offset);

    // int messagesToRead = 5;
    // int messagesAlreadyRead = 0;

    while (true) {
      ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000)); // 1000 milliseconds

      for (ConsumerRecord<String, String> record: records) {
        logger.info("[INFO] record key " + record.key());
        logger.info("[INFO] record value " + record.value());
        logger.info("[INFO] record partition " + record.partition());
        logger.info("[INFO] record offset " + record.offset());

        // messagesAlreadyRead += 1;
      }

      // if (messagesAlreadyRead >= messagesToRead) {
      //   consumer.close();
      //   break;
      // }
    }

    // logger.info("[INFO] Finished");
  }
}
