FROM eclipse-temurin:17-jre-focal

RUN mkdir -p /app
ARG JAR_FILE=./*.jar
COPY ${JAR_FILE} /app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
