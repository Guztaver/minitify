# Minitify Docker Setup

## Quick Start

### 1. Build and Run
```bash
# Build the Docker image
make build

# Start all services
make up

# Or for development with debug enabled
make dev
```

### 2. Access the Application
- **Application**: http://localhost:8080
- **API Docs**: http://localhost:8080/swagger-ui.html
- **Health Check**: http://localhost:8080/actuator/health
- **MongoDB**: localhost:27017

## Docker Compose Services

### MongoDB
- **Image**: mongo:8
- **Database**: minitify
- **Credentials**: minitify/minitify
- **Port**: 27017

### Minitify Application
- **Image**: guztaver/minitify:latest
- **Port**: 8080
- **Health Check**: /actuator/health

## Environment Variables

The application is configured with the following environment variables:

```yaml
environment:
  - SPRING_DATA_MONGODB_URI=mongodb://minitify:minitify@mongodb:27017/minitify
  - SPRING_DATA_MONGODB_DATABASE=minitify
  - SECURITY_JWT_SHARED-SECRET=my-secret-key-that-is-at-least-256-bits-long-for-hs256
  - SECURITY_JWT_ISSUER=minitify
  - SECURITY_JWT_EXPIRATION-MS=3600000
  - APP_URL=http://localhost:8080
```

## Development

### Development Override
The `docker-compose.override.yaml` provides additional development features:
- Debug port 5005 enabled
- Resource mounting for live reload
- Debug logging enabled

### Commands
```bash
# Start development environment
make dev

# View logs
make logs

# Stop services
make down

# Clean up everything
make clean

# Rebuild and restart
make restart
```

## Manual Docker Commands

```bash
# Build image
docker build -t guztaver/minitify:latest .

# Run with docker-compose
docker-compose up -d

# View logs
docker-compose logs -f minitify

# Stop services
docker-compose down
```

## Health Checks

Both services include health checks:
- **MongoDB**: Database ping test
- **Minitify**: HTTP health endpoint

## Volumes

- `mongo_data`: Persistent MongoDB data storage

## Networks

Services communicate via `minitify-network` bridge network.

## Production Deployment

For production deployment:
1. Update environment variables with secure values
2. Remove debug configurations
3. Use proper secrets management
4. Configure external MongoDB if needed

```bash
# Production deployment
docker-compose -f docker-compose.yaml up -d
```