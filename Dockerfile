# ----------------------------
# Stage 1: Build the Pomodoro Spring Boot app
# ----------------------------
FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app

# Copy pom.xml and download dependencies first (better caching)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the rest of the project files
COPY src ./src

# Build the project and skip tests
RUN mvn clean package -DskipTests

# ----------------------------
# Stage 2: Run the Pomodoro Spring Boot app
# ----------------------------
FROM openjdk:21-jdk-slim

WORKDIR /app

# Copy the JAR built in the previous stage (match any JAR)
COPY --from=build /app/target/*.jar app.jar

# Expose the default Spring Boot port (8080)
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
