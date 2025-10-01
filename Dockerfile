FROM maven:3.9-amazoncorretto-24 AS build-stage
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM amazoncorretto:24-alpine AS run-stage
WORKDIR /app
COPY --from=build-stage /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-Dspring.output.ansi.enabled=always", "-jar", "/app/app.jar"]