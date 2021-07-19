FROM juliocesarmidia/java14.0.2-maven3.8.1:latest
LABEL maintainer="Julio Cesar <julio@blackdevs.com.br>"

WORKDIR /usr/src/app

# for cache
RUN mkdir -p /root/.m2
VOLUME ["/root/.m2"]

COPY . .

# generate package
RUN mvn --batch-mode package --file pom.xml -Dmaven.repo.local=/root/.m2 -D skipTests

CMD java $JAVA_OPTIONS -jar /usr/src/app/target/app.jar
