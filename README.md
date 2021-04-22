# Kafka Java Project

This project is a test with Kafka using Java client, running everything inside docker containers.

## Up and Running

```bash
docker-compose up -d kafka zookeeper
docker-compose logs -f kafka zookeeper

docker-compose up -d --build java-kafka
docker-compose logs -f java-kafka
```
