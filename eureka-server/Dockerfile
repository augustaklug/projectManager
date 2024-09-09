FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY eureka-server/pom.xml .
COPY eureka-server/src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8761
ENTRYPOINT ["java", "-jar", "app.jar"]