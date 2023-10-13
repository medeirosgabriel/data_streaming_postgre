FROM maven:3.8.2-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn install -DskipTests
FROM openjdk:17-jdk-slim
COPY --from=build /app/target/orderservice-0.0.1-SNAPSHOT.jar /app/orderservice-0.0.1-SNAPSHOT.jar

# Define as variáveis de ambiente
CMD ["java", "-jar", "/app/orderservice-0.0.1-SNAPSHOT.jar"]
