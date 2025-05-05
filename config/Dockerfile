FROM eclipse-temurin:17-jdk-focal

WORKDIR /app

# Install Maven
RUN apt-get update && apt-get install -y maven

# Copy project files
COPY pom.xml ./
RUN mvn dependency:go-offline

COPY src ./src

RUN mvn clean package

CMD ["java", "-jar", "/app/target/config-0.0.1-SNAPSHOT.jar"]