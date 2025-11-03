# Simple Dockerfile that packages the application as-is
FROM eclipse-temurin:21-jre-alpine

# Install curl for health checks
RUN apk add --no-cache curl

# Create app user
RUN addgroup -g 1000 -S appuser && \
    adduser -S appuser -u 1000

# Set working directory
WORKDIR /app

# Copy the JAR (assuming it's built locally)
COPY build/libs/minitify-0.1.0-SNAPSHOT.jar /app/minitify.jar

# Copy application properties
COPY src/main/resources/application.properties /app/application.properties

# Change ownership
RUN chown -R appuser:appuser /app

# Switch to non-root user
USER appuser

# Expose port
EXPOSE 8080

# Add health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

# Set the entrypoint
ENTRYPOINT ["java", "-jar", "minitify.jar"]