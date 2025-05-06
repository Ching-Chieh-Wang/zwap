FROM maven:3.9.4-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn -pl config clean package -DskipTests
RUN ls -lh config/target/

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/config/target/config-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]