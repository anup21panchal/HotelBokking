# Stage 1: Build
FROM maven:3.9.8-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Stage 2: Run (Debian-based with certs + OpenSSL)
FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app

# Install CA certs + OpenSSL (needed for MongoDB Atlas TLS handshake)
RUN apt-get update && apt-get install -y ca-certificates openssl && update-ca-certificates && rm -rf /var/lib/apt/lists/*

COPY --from=build /app/target/hotel-booking-service-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

# Force Java to use TLSv1.2 (Atlas requires >= 1.2)
ENTRYPOINT ["java", "-Djdk.tls.client.protocols=TLSv1.2", "-jar", "app.jar", "--spring.profiles.active=pro"]
