# Bank Operations Microservices Architecture

## Overview
This project is a microservices-based architecture designed to handle customer management, transaction processing, and file uploads in a banking system. The system ensures scalability, fault tolerance, and maintainability using modern technologies such as **Spring Boot**, **Spring Cloud**, **RabbitMQ**, **Redis**, and **PostgreSQL**.

## Architecture
The system consists of the following microservices:

### 1. **Eureka Server**
- Service discovery using **Spring Cloud Netflix Eureka**.
- Registers all microservices and enables service-to-service communication.

### 2. **API Gateway (ms-gateway)**
- Routes external requests to the appropriate microservice.
- Handles cross-cutting concerns such as authentication, logging, and rate limiting.

### 3. **Customer Service (ms-customers)**
- Manages customer data (CRUD operations).
- Uses **Spring Data JPA** with **PostgreSQL** for persistence.
- Caches customer data in **Redis**.
- Publishes and listens to events using **RabbitMQ**.

### 4. **File Upload Service (ms-file-upload)**
- Handles document and photo uploads.
- Stores metadata in **PostgreSQL**.
- Uses **RabbitMQ** for asynchronous processing.

### 5. **Transaction Event Service (ms-transaction-events)**
- Processes financial transactions.
- Validates customer details via inter-service communication.
- Uses **RabbitMQ** for event-driven processing.
- Stores transactions in **PostgreSQL**.

## Technologies Used
- **Spring Boot 3.0.0** (for building microservices)
- **Spring Cloud Netflix Eureka** (service discovery)
- **Spring Cloud Gateway** (API gateway management)
- **Spring Data JPA** with **PostgreSQL** (data persistence)
- **Redis** (caching and fast lookups)
- **RabbitMQ** (message queue for async processing)
- **Docker & Docker Compose** (containerization)

## Setup and Deployment
### Prerequisites
Ensure you have the following installed:
- Docker & Docker Compose
- Java 17
- Maven or Gradle

### Running the Application
To start all microservices, use Docker Compose:
```bash
docker-compose up --build
```
This will launch all microservices along with dependencies like PostgreSQL, Redis, and RabbitMQ.

### Running Services Individually
If you prefer to run services manually:
1. Start dependencies (PostgreSQL, Redis, RabbitMQ) via Docker Compose:
   ```bash
   docker-compose up -d postgres redis rabbitmq
   ```
2. Start each microservice separately:
   ```bash
   cd eureka-server && ./gradlew bootRun
   cd ms-gateway && ./gradlew bootRun
   cd ms-customers && mvn spring-boot:run
   cd ms-file-upload && mvn spring-boot:run
   cd ms-transaction-events && mvn spring-boot:run
   ```

## API Endpoints
### Customer Service (ms-customers)
- `GET /api/v1/customers` - List all customers
- `POST /api/v1/customers` - Create a new customer
- `GET /api/v1/customers/{id}` - Get customer by ID
- `DELETE /api/v1/customers/{id}` - Delete customer

### File Upload Service (ms-file-upload)
- `POST /api/v1/files/upload` - Upload a file
- `GET /api/v1/files/{photoId}` - Get file details
- `DELETE /api/v1/files/{photoId}` - Delete a file

### Transaction Service (ms-transaction-events)
- `POST /api/v1/transactions` - Create a transaction
- `GET /api/v1/transactions/{id}` - Get transaction details
- `GET /api/v1/transactions` - List all transactions

## Exception Handling
- **GlobalExceptionHandler** is implemented to handle exceptions across all services.
- Each microservice has specific exceptions like `CustomerNotFoundException`, `PhotoStorageException`, and `TransactionNotFoundException`.

## Logging & Monitoring
- **AOP-based Logging** is implemented to track service calls.
- Actuator endpoints are enabled for health checks and monitoring.

## Future Improvements
- Implementing API security using **OAuth2 or JWT**.
- Adding unit and integration tests.
- CI/CD pipeline setup using **GitHub Actions or Jenkins**.

## Contributors
- **Vusal Atashov** - System Architecture & Development

---
This README provides a structured overview of the project, ensuring easy understanding and deployment of the system.

