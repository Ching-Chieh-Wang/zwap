FROM eclipse-temurin:17-jdk-jammy AS builder
WORKDIR /opt/app
COPY pom.xml ./
RUN apt-get update && apt-get install -y maven
RUN mvn dependency:go-offline
COPY ./src ./src
RUN ./mvnw clean install

FROM eclipse-temurin:17-jre-jammy
WORKDIR /opt/app
EXPOSE 8080
COPY --from=builder /opt/app/target/*.jar /opt/app/app.jar
ENTRYPOINT ["java", "-jar", "/opt/app/app.jar"]
