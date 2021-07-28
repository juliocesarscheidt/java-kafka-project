package com.github.juliocesarscheidt.kafka;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;

import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.Hosts;
import com.twitter.hbc.core.HttpHosts;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.event.Event;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;

public class TwitterProducer extends Producer {
  public TwitterProducer(String bootstrapServers, String topic, final Logger logger) {
    super(bootstrapServers, topic, logger);
  }

  @Override
  public void start() {
    // creating queues
    BlockingQueue<String> msgQueue = new LinkedBlockingQueue<String>(1000);

    Hosts hosebirdHosts = new HttpHosts(Constants.STREAM_HOST);
    StatusesFilterEndpoint hosebirdEndpoint = new StatusesFilterEndpoint();

    List<String> searches = Arrays.asList("Bitcoin");
    hosebirdEndpoint.trackTerms(searches);

    String consumerKey = System.getenv("TWITTER_CONSUMER_KEY") != null ?
      System.getenv("TWITTER_CONSUMER_KEY") : "";
    String consumerSecret = System.getenv("TWITTER_CONSUMER_SECRET") != null ?
      System.getenv("TWITTER_CONSUMER_SECRET") : "";
    String accessToken = System.getenv("TWITTER_ACCESS_TOKEN") != null ?
      System.getenv("TWITTER_ACCESS_TOKEN") : "";
    String accessTokenSecret = System.getenv("TWITTER_ACCESS_TOKEN_SECRET") != null ?
      System.getenv("TWITTER_ACCESS_TOKEN_SECRET") : "";

    // These secrets should be read from a config file
    Authentication hosebirdAuth = new OAuth1(consumerKey, consumerSecret, accessToken, accessTokenSecret);

    // create a client to fetch data from twitter
    ClientBuilder builder = new ClientBuilder()
      .name("hosebird-client-01")                              // optional: mainly for the logs
      .hosts(hosebirdHosts)
      .authentication(hosebirdAuth)
      .endpoint(hosebirdEndpoint)
      .processor(new StringDelimitedProcessor(msgQueue));

    com.twitter.hbc.core.Client hosebirdClient = builder.build();
    // Attempts to establish a connection.
    hosebirdClient.connect();

    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      logger.info("Stopping Application :: TwitterProducer");

      logger.info("Shutting down Hosebird Client :: TwitterProducer");
      hosebirdClient.stop();

      logger.info("Flushing and Closing :: TwitterProducer");
      this.producer.flush();
      this.producer.close();
    }));

    while (!hosebirdClient.isDone()) {
      String msg = null;

      try {
        msg = msgQueue.poll(10, TimeUnit.SECONDS); // 10 seconds polling

      } catch (InterruptedException e) {
        e.printStackTrace();
        hosebirdClient.stop();
      }

      if (msg != null) {
        logger.info(msg);
        this.sendMessage(this.producer, this.topic, null, msg, this.logger);
      }
    }
  }
}
