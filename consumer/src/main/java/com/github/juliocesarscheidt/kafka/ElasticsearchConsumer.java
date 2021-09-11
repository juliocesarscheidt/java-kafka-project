package com.github.juliocesarscheidt.kafka;

import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;

import org.apache.http.HttpHost;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ElasticsearchConsumer extends Consumer {
  final Logger logger = LoggerFactory.getLogger(ElasticsearchConsumer.class);

  private String elasticsearchHost;
  private Integer elasticsearchPort;

  protected RestHighLevelClient elasticClient;

  public ElasticsearchConsumer(String bootstrapServers, String topic, final Logger logger, String elasticsearchHost, Integer elasticsearchPort) {
    super(bootstrapServers, topic, logger);

    this.elasticsearchHost = elasticsearchHost;
    this.elasticsearchPort = elasticsearchPort;

    this.elasticClient = new RestHighLevelClient(RestClient.builder(
      new HttpHost(this.elasticsearchHost, this.elasticsearchPort, "http")
    ));
  }

  @Override
  public void start() {
    this.consumer.subscribe(Arrays.asList(this.topic));

    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      logger.info("Stopping Application :: ElasticsearchConsumer");

      this.consumer.commitSync();
      logger.info("Offsets have been committed");

      logger.info("Flushing and Closing :: ElasticsearchConsumer");
      this.consumer.close();
    }));

    while (true) {
      ConsumerRecords<String, String> records = this.consumer.poll(Duration.ofMillis(5000)); // 5000 milliseconds

      Integer recordsCount = records.count();
      this.logger.info("Received " + recordsCount + " records");

      // create a new bulk request
      BulkRequest bulkRequest = new BulkRequest();

      for (ConsumerRecord<String, String> record: records) {
        this.logger.info("[INFO] record key " + record.key());
        this.logger.info("[INFO] record value " + record.value());
        this.logger.info("[INFO] record partition " + record.partition());
        this.logger.info("[INFO] record offset " + record.offset());

        String messageString = record.value();

        // generica kafka id
        // String kafkaId = record.topic() + "_" + record.partition() + "_" + record.offset();
        try {
          // id from tweet
          String id = extractValueFromJson(record.value(), "id_str");

          @SuppressWarnings("deprecation")
          IndexRequest req = new IndexRequest("twitter", "tweets")
            .id(id)
            .source(messageString, XContentType.JSON);

          bulkRequest.add(req);

        } catch (NullPointerException e) {
          this.logger.error(e.getMessage());
        }
      }

      if (recordsCount > 0) {
        try {
          BulkResponse resp = elasticClient.bulk(bulkRequest, RequestOptions.DEFAULT);
          logger.info(resp.toString());

          this.consumer.commitSync();
          logger.info("Offsets have been committed");

          Thread.sleep(1000);

        } catch (IOException | InterruptedException e) {
          this.logger.error(e.getMessage());
        }
      }
    }
  }
}
