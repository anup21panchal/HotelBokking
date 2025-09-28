# Stage 1: Build
FROM maven:3.9.8-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Stage 2: Run
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

# 👇 Fix: install CA certificates for SSL (needed for MongoDB Atlas)
RUN apk --no-cache add ca-certificates && update-ca-certificates

COPY --from=build /app/target/hotel-booking-service-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=pro"]
