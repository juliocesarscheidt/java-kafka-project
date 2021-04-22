FROM openjdk:12 AS builder

WORKDIR /opt

# download maven
RUN curl -L \
    --url https://mirror.nbtelecom.com.br/apache/maven/maven-3/3.8.1/binaries/apache-maven-3.8.1-bin.tar.gz \
    --output apache-maven-3.8.1-bin.tar.gz
RUN tar xzvf apache-maven-3.8.1-bin.tar.gz && \
    rm apache-maven-3.8.1-bin.tar.gz

ENV PATH=$PATH:/opt/apache-maven-3.8.1/bin
ENV M2_HOME=/opt/apache-maven-3.8.1/

WORKDIR /app

COPY . .
RUN mvn --batch-mode package --file pom.xml -D skipTests

EXPOSE 8080

CMD java $JAVA_OPTIONS -jar /app/target/kafka-project-0.0.1.jar
