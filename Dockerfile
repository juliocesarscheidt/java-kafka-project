FROM openjdk:12 AS builder

# for debian/ubuntu
# apt-get install ca-certificates-java

# JAVA_HOME=/usr/java/openjdk-12

WORKDIR /opt

# download maven
RUN curl -L \
    --url https://mirror.nbtelecom.com.br/apache/maven/maven-3/3.8.1/binaries/apache-maven-3.8.1-bin.tar.gz \
    --output apache-maven-3.8.1-bin.tar.gz
RUN tar xzvf apache-maven-3.8.1-bin.tar.gz && \
    rm apache-maven-3.8.1-bin.tar.gz

ENV PATH=$PATH:/opt/apache-maven-3.8.1/bin

WORKDIR /build

COPY . .

RUN mvn clean compile && \
    mvn package -DskipTests

# execution stage
FROM openjdk:12 AS runner

WORKDIR /app

# copy jar artifact
COPY --from=builder /build/target/kafka-project-0.0.1.jar kafka-project-0.0.1.jar

EXPOSE 8080

CMD java $JAVA_OPTIONS -jar /app/kafka-project-0.0.1.jar
