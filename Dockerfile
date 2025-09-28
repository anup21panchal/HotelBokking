# Stage 1: Build
FROM maven:3.9.8-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Stage 2: Run (use Debian-based JDK with full SSL support)
FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app

# Copy built JAR
COPY --from=build /app/target/hotel-booking-service-0.0.1-SNAPSHOT.jar app.jar

# Expose port
EXPOSE 8080

# Run app with prod profile
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=pro"]
