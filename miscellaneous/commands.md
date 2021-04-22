# Useful Commands

```bash
# for debian/ubuntu
# apt-get install ca-certificates-java

# JAVA_HOME=/usr/java/openjdk-12
# M2_HOME=/opt/apache-maven-3.8.1/

# maven
mvn -X compile
mvn clean compile
mvn -X clean compile
mvn package -DskipTests
mvn -X package -DskipTests
mvn -X --batch-mode package --file pom.xml


# java
java $JAVA_OPTIONS -XX:+PrintFlagsFinal \
  -jar /app/kafka-project-0.0.1.jar

java $JAVA_OPTIONS \
  -jar /app/kafka-project-0.0.1.jar

java -classpath src \
  -jar /app/kafka-project-0.0.1.jar


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
