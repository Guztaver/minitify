# Minitify

Minitify is a Spring Boot application designed for managing music albums and artists. It provides RESTful APIs for CRUD
operations on albums and artists, with user authentication via JWT tokens. The application uses PostgreSQL as the
database, Flyway for database migrations, and includes OpenAPI documentation with Scalar UI.

## Features

- User authentication and authorization with JWT
- CRUD operations for albums and artists
- RESTful API endpoints
- Database migrations with Flyway
- OpenAPI/Swagger documentation with Scalar UI
- Support for PostgreSQL and H2 (for testing)
- Native image support with GraalVM

## Tech Stack

- **Java**: 21
- **Spring Boot**: 3.3.5
- **Spring Security**: OAuth2 Resource Server
- **Spring Data JPA**: For data persistence
- **Spring WebFlux**: Reactive web framework
- **PostgreSQL**: Primary database
- **H2**: In-memory database for testing
- **Flyway**: Database migration tool
- **OpenAPI**: API documentation
- **Scalar**: Modern API documentation UI
- **GraalVM**: For native compilation

## Prerequisites

- Java 21 or higher
- Gradle (or use the included Gradle wrapper)
- PostgreSQL database
- Docker (optional, for running PostgreSQL via Docker Compose)

## Setup

1. **Clone the repository**:
   ```bash
   git clone <repository-url>
   cd minitify
   ```

2. **Database Setup**:
    - Ensure PostgreSQL is running.
    - Create a database named `minitify`.
    - Update `src/main/resources/application.properties` with your database credentials if necessary.

   Alternatively, use Docker Compose to run PostgreSQL:
   ```bash
   docker-compose up -d
   ```

3. **Build the project**:
   ```bash
   ./gradlew build
   ```

## Running the Application

1. **Run the application**:
   ```bash
   ./gradlew bootRun
   ```

   The application will start on `http://localhost:8080`.

2. **Access the API Documentation**:
    - Scalar UI: `http://localhost:8080/docs`
    - Swagger UI: `http://localhost:8080/swagger-ui.html`

## API Endpoints

- **Albums**: `/albums` - Manage albums
- **Artists**: `/artists` - Manage artists
- **Login**: `/login` - User authentication

All endpoints require Bearer token authentication.

## Configuration

Configuration is managed via `application.properties`. Key settings include:

- Database connection details
- JWT secret and issuer
- OpenAPI and Scalar configurations

## Testing

Run tests with:

```bash
./gradlew test
```

## Building for Production

- **JAR**: `./gradlew bootJar`
- **Native Image**: `./gradlew nativeCompile` (requires GraalVM)

## Contributing

1. Fork the repository.
2. Create a feature branch.
3. Make your changes.
4. Run tests.
5. Submit a pull request.

## License

This project is licensed under the MIT License.
