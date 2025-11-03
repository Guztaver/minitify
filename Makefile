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

# Deploy to Fly.io (GRU region - cheapest)
fly-deploy:
	@echo "🚀 Deploying to Fly.io (GRU region)..."
	./deploy-fly.sh

# Deploy to Fly.io with custom app name
fly-deploy-custom:
	@if [ -z "$(APP_NAME)" ]; then \
		echo "❌ Please set APP_NAME environment variable"; \
		echo "Usage: APP_NAME=myapp make fly-deploy-custom"; \
		exit 1; \
	fi
	@echo "🚀 Deploying $(APP_NAME) to Fly.io (GRU region)..."
	APP_NAME=$(APP_NAME) ./deploy-fly.sh