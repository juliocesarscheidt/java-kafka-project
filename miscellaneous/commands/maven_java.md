# Some Commands

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


# java (producer)
java $JAVA_OPTIONS -XX:+PrintFlagsFinal \
  -jar /app/kafka-project-producer-0.0.1.jar

java $JAVA_OPTIONS \
  -jar /app/kafka-project-producer-0.0.1.jar


# java (consumer)
java $JAVA_OPTIONS -XX:+PrintFlagsFinal \
  -jar /app/kafka-project-consumer-0.0.1.jar

java $JAVA_OPTIONS \
  -jar /app/kafka-project-consumer-0.0.1.jar
```
