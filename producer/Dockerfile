FROM openjdk:12 AS builder

WORKDIR /opt

# download maven
RUN curl --silent -L \
    --url https://ftp.unicamp.br/pub/apache/maven/maven-3/3.8.1/binaries/apache-maven-3.8.1-bin.tar.gz \
    --output apache-maven-3.8.1-bin.tar.gz
RUN tar xzvf apache-maven-3.8.1-bin.tar.gz && \
    rm apache-maven-3.8.1-bin.tar.gz

# put maven on PATH
ENV PATH=$PATH:/opt/apache-maven-3.8.1/bin
ENV M2_HOME=/opt/apache-maven-3.8.1/

ENV ARTIFACT_ID=kafka-project-producer
ENV ARTIFACT_VERSION=0.0.1

WORKDIR /app

COPY . .
# generate package
RUN mvn --batch-mode package --file pom.xml -D skipTests

CMD java $JAVA_OPTIONS -jar "/app/target/${ARTIFACT_ID}-${ARTIFACT_VERSION}.jar"
