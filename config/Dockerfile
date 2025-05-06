# ---- Build stage ----
FROM maven:3.9.4-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# DEBUG: dump the directory and JAR contents
RUN echo "=== PROJECT ROOT ===" && ls -R . \
 && echo "=== TARGET CONTENTS ===" && ls -lh target/ \
 && echo "=== JAR CONTENTS ===" && unzip -l target/config-0.0.1-SNAPSHOT.jar

# ---- Run stage ----
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/config-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]