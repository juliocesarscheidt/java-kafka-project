# Some Commands

```bash
# kafka docker
docker-compose exec kafka kafka-topics \
  --zookeeper zookeeper:2181 --create --topic topic_0 --partitions 1 --replication-factor 1 --if-not-exists

docker-compose exec kafka kafka-topics \
  --zookeeper zookeeper:2181 --list

# produce some message
docker-compose exec kafka kafka-console-producer --broker-list 127.0.0.1:9092 --topic topic_0


# docker
docker container run --rm -it --volume $PWD/:/app/ --name java openjdk:12 sh

docker image build --tag juliocesarmidia/java-kafka:latest .

docker container run --rm \
  --name java-kafka -m 800M \
  -e JAVA_OPTIONS='-Xmx400m' -p 8000:8080 \
  juliocesarmidia/java-kafka:latest
```
