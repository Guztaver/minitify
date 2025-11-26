# Multi-stage Dockerfile that builds and packages the application
FROM eclipse-temurin:21-jdk-alpine AS builder

# Install curl and git (if needed for build)
RUN apk add --no-cache curl git

# Set working directory for build
WORKDIR /build

# Copy source code
COPY . .

# Make gradlew executable and build the application
RUN chmod +x gradlew && ./gradlew bootJar --no-daemon

# Final stage
FROM eclipse-temurin:21-jre-alpine

# Install curl for health checks
RUN apk add --no-cache curl

# Create app user
RUN addgroup -g 1000 -S appuser && \
    adduser -S appuser -u 1000

# Set working directory
WORKDIR /app

# Copy the built JAR from builder stage
COPY --from=builder /build/build/libs/minitify-0.1.0-SNAPSHOT.jar /app/minitify.jar

# Copy application properties
COPY src/main/resources/application.properties /app/application.properties

# Copy .env file if it exists (optional)
COPY .env /app/.env

# Change ownership
RUN chown -R appuser:appuser /app

# Load environment variables from .env file if it exists
RUN if [ -f /app/.env ]; then \
        # Export variables from .env file
        export $(cat /app/.env | grep -v '^#' | xargs); \
    fi

# Switch to non-root user
USER appuser

# Expose port
EXPOSE 8080

# Add health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

# Copy startup script that loads .env variables
COPY docker-entrypoint.sh /app/docker-entrypoint.sh
RUN chmod +x /app/docker-entrypoint.sh
RUN chown appuser:appuser /app/docker-entrypoint.sh

# Set the entrypoint
ENTRYPOINT ["/app/docker-entrypoint.sh"]