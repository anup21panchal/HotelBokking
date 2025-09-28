# Stage 1: Build
FROM maven:3.9.8-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Stage 2: Run
FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app

# Install CA certificates (important for Atlas TLS handshake)
RUN apt-get update && apt-get install -y ca-certificates && update-ca-certificates

COPY --from=build /app/target/hotel-booking-service-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

# Force TLS 1.2 to avoid internal_error
ENTRYPOINT ["java","-Djdk.tls.client.protocols=TLSv1.2","-jar","app.jar","--spring.profiles.active=pro"]
