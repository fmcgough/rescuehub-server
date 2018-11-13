FROM openjdk:8-jdk-alpine
VOLUME /tmp
ARG JAR_FILE
COPY build/libs/${JAR_FILE} app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]

