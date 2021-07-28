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
  protected String bootstrapServers;
  protected String topic;
  protected Logger logger;
  protected KafkaProducer<String, String> producer;

  public Producer(String bootstrapServers, String topic, final Logger logger) {
    this.bootstrapServers = bootstrapServers;
    this.topic = topic;
    this.logger = logger;

    // create the producer
    this.producer = this.createProducer();
  }

  public KafkaProducer<String, String> createProducer() {
    // create the config
    Properties config = new Properties();

    config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, this.bootstrapServers);

    // to send strings we need a string serializer
    config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
    config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

    // enable.idempotence -> default=false
    // Enabling idempotence requires:
    // - max.in.flight.requests.per.connection to be less than or equal to 5
    // - retries to be greater than 0
    // - acks must be all
    config.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);

    // acks
    // 0 = means no acks (no response is requested)
    // 1 = means leader acks (leader response is requested)
    // all = means leader and replicas acks (leader and replicas responses are requested)
    config.put(ProducerConfig.ACKS_CONFIG, "all");

    // retries -> default=Integer.MAX_VALUE (2 ^ 31 - 1 = 2.147.483.647)
    config.put(ProducerConfig.RETRIES_CONFIG, Integer.toString(Integer.MAX_VALUE));

    // retry.backoff.ms -> default=100 ms
    config.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, 100);

    // delivery.timeout.ms -> default=(120 * 1000) ms // 120 secs = 2 mins | ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG
    config.put("delivery.timeout.ms", 30 * 1000); // 30 secs

    // max.in.flight.requests.per.connection -> default=5
    config.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, 5);

    // buffer.memory -> default=33554432 bytes // (32 * 1024 * 1024) bytes
    config.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432); // 32 MB

    // max.block.ms -> default=(60 * 1000) ms // 60000 ms = 60 secs
    config.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, 33554432); // 32 MB

    // compression.type -> default=none // none, gzip, snappy, or lz4.
    config.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "snappy"); // less compression than gzip, but faster

    // batch.size -> default=16384 bytes // (16 * 1024) bytes
    config.put(ProducerConfig.BATCH_SIZE_CONFIG, 32768); // 32 KB

    // linger.ms -> default=0 ms (artificial delay)
    config.put(ProducerConfig.LINGER_MS_CONFIG, 5); // 5 ms

    // create the producer
    KafkaProducer<String, String> producer = new KafkaProducer<String, String>(config);

    return producer;
  }

  public void sendMessage(KafkaProducer<String, String> producer, String topic, String key, String message, final Logger logger) {
    // send a message to topic, asynchronously
    ProducerRecord<String, String> record = new ProducerRecord<String, String>(topic, key, message);

    producer.send(record, new Callback() {
      @Override
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
    String message = "Hello World";

    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      logger.info("Stopping Application :: Producer");

      logger.info("Flushing and Closing :: Producer");
      this.producer.flush();
      this.producer.close();
    }));

    // send data to a topic
    for (int i = 0; i < 5; i ++) {
      // the partition will be chosen according to the key, same key = same partition
      String key = "id_" + i;
      String value = message + " :: " + i;

      this.sendMessage(this.producer, this.topic, key, value, this.logger);
    }
  }
}
