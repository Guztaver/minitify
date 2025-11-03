.PHONY: help build push up down logs clean

# Default target
help:
	@echo "Available commands:"
	@echo "  build     - Build Docker image"
	@echo "  push      - Push Docker image to registry"
	@echo "  up        - Start services with docker-compose"
	@echo "  down      - Stop services"
	@echo "  logs      - Show logs"
	@echo "  clean     - Clean up containers and images"
	@echo "  dev       - Start development environment"

# Build Docker image
build:
	./build-and-push.sh

# Push to Docker Hub (requires login)
push:
	docker push guztaver/minitify:latest

# Start services
up:
	docker-compose up -d

# Start development environment
dev:
	docker-compose -f docker-compose.yaml -f docker-compose.override.yaml up -d

# Stop services
down:
	docker-compose down

# Show logs
logs:
	docker-compose logs -f

# Clean up
clean:
	docker-compose down -v
	docker system prune -f

# Rebuild and restart
restart: build up
	@echo "✅ Services restarted with new image"