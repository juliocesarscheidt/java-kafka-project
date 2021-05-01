# Kafka Java Project

![build](https://github.com/juliocesarscheidt/java-kafka-project/actions/workflows/build.yml/badge.svg)

This project is a test with Kafka using Java client, running everything inside docker containers.

## Up and Running

```bash
docker-compose up -d kafka zookeeper
docker-compose logs -f kafka zookeeper

docker-compose up -d kafka-boot
docker-compose logs -f kafka-boot

docker-compose up -d --build kafka-producer kafka-consumer
docker-compose logs -f kafka-producer kafka-consumer
```
