FROM openjdk:17

RUN mkdir /app
WORKDIR /app
COPY target/uberjar/hs-0.1.0-SNAPSHOT-standalone.jar /app
COPY config.edn /app

ENTRYPOINT ["java", "-jar", "/app/hs-0.1.0-SNAPSHOT-standalone.jar"]
