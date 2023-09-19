FROM maven:3.8.2-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn install -DskipTests
FROM openjdk:17-jdk-slim
COPY --from=build /app/target/job-0.0.1-SNAPSHOT.jar /app/job-0.0.1-SNAPSHOT.jar

# Define as variáveis de ambiente

ENV DB_HOST=postgres
ENV DB_PORT=5432
ENV DB_NAME=dockerdb
ENV DB_USERNAME=dockeruser
ENV DB_PASSWORD=dockerpassword
CMD ["java", "-jar", "/app/job-0.0.1-SNAPSHOT.jar"]
