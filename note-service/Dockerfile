FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY note-service/pom.xml .
COPY note-service/src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]