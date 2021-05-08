FROM openjdk:14.0.2
LABEL maintainer="Julio Cesar <julio@blackdevs.com.br>"

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

# docker image build -t juliocesarmidia/java14.0.2-maven3.8.1:latest -f maven.Dockerfile .
# docker image push juliocesarmidia/java14.0.2-maven3.8.1:latest
