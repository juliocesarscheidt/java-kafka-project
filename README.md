# Kafka Java Project

![Build PR](https://github.com/juliocesarscheidt/java-kafka-project/actions/workflows/build_pr.yml/badge.svg)
![Build Push](https://github.com/juliocesarscheidt/java-kafka-project/actions/workflows/build_push.yml/badge.svg)

This project was created with Kafka using Java client, with a producer and a consumer, running everything inside docker containers and the dependencies are managed by Maven.

Its CI it is made with Github Actions.

## Up and Running

```bash
docker-compose up -d kafka zookeeper
docker-compose logs -f kafka zookeeper

docker-compose up -d --build elastic
docker-compose logs -f elastic

docker-compose up -d --build kafka-producer
docker-compose logs -f kafka-producer

docker-compose up -d --build kafka-consumer
docker-compose logs -f kafka-consumer
```

## Docs

Kafka Producer configuration:

<https://jaceklaskowski.gitbooks.io/apache-kafka/content/kafka-producer-ProducerConfig.html>
