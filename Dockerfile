# Use lightweight Java 17 image
FROM eclipse-temurin:17-jdk-alpine

# Set working directory
WORKDIR /app

# Copy built jar into container
COPY target/hotel-booking-service-0.0.1-SNAPSHOT.jar app.jar

# Expose app port
EXPOSE 8080

# Run with production profile
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=pro"]
