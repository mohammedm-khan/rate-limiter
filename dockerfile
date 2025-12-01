# Stage 1: Build with JDK 24
FROM openjdk:24-jdk as builder
WORKDIR /workspace/app

# Copy build files
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

# Build the application
RUN ./mvnw clean package -DskipTests

# Stage 2: Runtime with JRE
FROM openjdk:24-jdk-slim
WORKDIR /app

# Copy built jar from builder
COPY --from=builder /workspace/app/target/*.jar app.jar

# Set environment variables
ENV JAVA_OPTS=""
ENV SPRING_PROFILES_ACTIVE="docker"

# Expose port
EXPOSE 8080

# Run the application
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar /app/app.jar"]