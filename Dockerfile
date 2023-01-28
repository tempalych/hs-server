FROM openjdk:17

# RUN mkdir /app
# WORKDIR /app
COPY target/uberjar/hs-0.1.0-SNAPSHOT-standalone.jar /hs-0.1.0-SNAPSHOT-standalone.jar
COPY config.edn /config.edn

ENTRYPOINT ["java", "-jar", "/hs-0.1.0-SNAPSHOT-standalone.jar"]
