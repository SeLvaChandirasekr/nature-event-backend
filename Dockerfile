# Multi-stage build for Java 21 Spring Boot
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app

# Copy all project files
COPY . .

# Build the jar
RUN chmod +x mvnw && ./mvnw clean package -DskipTests

# Run stage with lightweight JRE
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copy built jar from build stage
COPY --from=build /app/target/nature-event-backend-0.0.1-SNAPSHOT.jar app.jar

# Render injects the PORT environment variable
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
