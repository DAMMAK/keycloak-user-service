# Keycloak User Service

A Spring Boot microservice for user management with Keycloak integration, providing secure authentication and user operations.

## Overview

This service provides a RESTful API for user management operations integrated with Keycloak as an identity and access management solution. It handles user registration, authentication, profile management, and administrative functions.

## Features

- User registration and authentication
- Token-based authentication with JWT
- User profile management
- Role-based access control
- Admin user management capabilities
- Integration with Keycloak for identity management
- Swagger API documentation

## Tech Stack

- Java 17
- Spring Boot 3.5.3
- Spring Security with OAuth2 Resource Server
- Spring Data JPA
- PostgreSQL
- Keycloak 23.0.1
- Flyway for database migrations
- MapStruct for object mapping
- Lombok for reducing boilerplate code
- Springdoc OpenAPI for API documentation
- Docker for containerization

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- Docker and Docker Compose
- PostgreSQL (can be run via Docker)
- Keycloak (can be run via Docker)

## Getting Started

### Clone the Repository

```bash
git clone https://github.com/yourusername/keycloak-user-service.git
cd keycloak-user-service
```

### Running with Docker Compose

The easiest way to run the application is using Docker Compose:

```bash
docker-compose up -d
```

This will start:
- PostgreSQL database
- Keycloak server
- User Service application

### Running Locally

1. Start PostgreSQL and Keycloak using Docker Compose:

```bash
docker-compose up -d postgres keycloak
```

2. Build the application:

```bash
mvn clean package -DskipTests
```

3. Run the application:

```bash
java -jar target/keyclock-user-0.0.1-SNAPSHOT.jar
```

Or using Maven:

```bash
mvn spring-boot:run
```

## Configuration

The application can be configured through the following properties files:
- `application.yml` - Main configuration
- `application-dev.yml` - Development environment configuration
- `application-prod.yml` - Production environment configuration

Key configuration properties:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/user_db
    username: postgres
    password: admin_password_123

keycloak:
  auth-server-url: http://localhost:8081
  realm: user-service
  client-id: user-service-rest-api
  client-secret: your-client-secret
```

## API Endpoints

The service exposes the following main endpoints:

### Authentication

- `POST /user-service/api/v1/auth/register` - Register a new user
- `POST /user-service/api/v1/auth/login` - Login user
- `POST /user-service/api/v1/auth/refresh` - Refresh access token
- `POST /user-service/api/v1/auth/logout` - Logout user

### User Management

- `GET /user-service/api/v1/users` - Get all users (Admin only)
- `GET /user-service/api/v1/users/{userId}` - Get user by ID
- `PUT /user-service/api/v1/users/{userId}` - Update user
- `DELETE /user-service/api/v1/users/{userId}` - Delete user (Admin only)

## API Documentation

Swagger UI is available at:
```
http://localhost:8080/user-service/swagger-ui.html
```

API documentation is available at:
```
http://localhost:8080/user-service/api-docs
```

## Database Migrations

The application uses Flyway to manage database migrations. Migration scripts are located in:
```
src/main/resources/db/migration
```

## Monitoring

The application exposes the following actuator endpoints:
```
http://localhost:8080/user-service/actuator/health
http://localhost:8080/user-service/actuator/info
http://localhost:8080/user-service/actuator/metrics
http://localhost:8080/user-service/actuator/prometheus
```

## Development

### Building the Project

```bash
mvn clean install
```

### Running Tests

```bash
mvn test
```

### Code Structure

- `controller` - REST API controllers
- `service` - Business logic implementation
- `repository` - Data access layer
- `entity` - JPA entities
- `dto` - Data transfer objects
- `config` - Application configuration
- `exception` - Exception handling

