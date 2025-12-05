# Spring Boot Full Stack AI Microservices Project

## Overview
This project is a comprehensive microservices-based application designed for a fitness platform. It leverages a modern tech stack including Spring Boot, Spring Cloud, Reactive programming (WebFlux), and AI integration to provide personalized recommendations. The system is designed to be scalable, resilient, and responsive.

## Architecture
The project follows a microservices architecture with the following key components:

*   **Service Discovery**: Netflix Eureka Server for registering and discovering services.
*   **API Gateway**: Spring Cloud Gateway as the single entry point for all client requests, handling routing and cross-cutting concerns.
*   **Configuration Management**: Spring Cloud Config Server for centralized configuration management.
*   **Microservices**:
    *   **User Service**: Manages user profiles and data using a reactive approach.
    *   **Activity Service**: Tracks user activities (workouts, exercises) and publishes events.
    *   **AI Service**: Consumes activity data and provides personalized recommendations using AI logic.

## Tech Stack
*   **Java**: 17
*   **Framework**: Spring Boot 3.5.6, Spring Cloud 2025.0.0 / 2023.0.3
*   **Database**:
    *   **MongoDB**: For storing activity data (Activity Service, AI Service).
    *   **PostgreSQL**: For storing user data (User Service).
*   **Messaging**: Apache Kafka (Spring Kafka) for event-driven communication between services.
*   **Reactive Programming**: Spring WebFlux and R2DBC for non-blocking I/O.
*   **Build Tool**: Maven

## Microservices Detail

### 1. Eureka Server (`eureka-server`)
*   **Port**: 8761 (Default)
*   **Description**: Acts as the service registry. All other services register themselves here upon startup.

### 2. Config Server (`config-server`)
*   **Port**: 8888 (Default)
*   **Description**: Centralizes configuration for all microservices. It can be backed by a Git repository or local file system.

### 3. Gateway Service (`gateway-service`)
*   **Port**: 8080 (Default)
*   **Description**: The API Gateway that routes requests to the appropriate microservices. It also handles security (OAuth2 Resource Server) and cross-cutting concerns.

### 4. User Service (`user-service`)
*   **Description**: Handles user registration, profile management, and authentication.
*   **Key Tech**: Spring WebFlux, R2DBC (PostgreSQL), ModelMapper.
*   **Database**: PostgreSQL.

### 5. Activity Service (`activity-service`)
*   **Description**: Manages fitness activities. It records workouts and publishes "Activity Created" events to Kafka.
*   **Key Tech**: Spring WebFlux, Spring Data MongoDB, Spring Kafka.
*   **Database**: MongoDB.
*   **Key Models**: `Activity`, `ActivityType`.

### 6. AI Service (`ai-service`)
*   **Description**: Provides intelligent recommendations. It listens to activity events and generates recommendations based on user history.
*   **Key Tech**: Spring WebFlux, Spring Data MongoDB, Spring Kafka.
*   **Database**: MongoDB.
*   **Key Endpoints**:
    *   `GET /api/recommendations/user/{userId}`: Get recommendations for a user.
    *   `GET /api/recommendations/activity/{activityId}`: Get recommendations based on a specific activity.

## Getting Started

### Prerequisites
*   **Java 17** SDK installed.
*   **Maven** installed.
*   **Docker** (optional, but recommended for running databases and Kafka).
*   **MongoDB** running locally or via Docker.
*   **PostgreSQL** running locally or via Docker.
*   **Apache Kafka** running locally or via Docker.

### Installation & Running

1.  **Clone the repository**:
    ```bash
    git clone <repository-url>
    cd Spring-Boot-Full-Stack-AI-Microservices-Project
    ```

2.  **Build the project**:
    ```bash
    mvn clean install
    ```
    *Note: This will build all modules.*

3.  **Start the Infrastructure Services** (in this order):
    *   Start **Eureka Server**.
    *   Start **Config Server**.
    *   Start **Gateway Service**.

4.  **Start the Microservices**:
    *   Start **User Service**.
    *   Start **Activity Service**.
    *   Start **AI Service**.

### API Usage
Once all services are running, you can access the APIs through the Gateway Service (default port 8080).

*   **User Service**: `/user-service/**`
*   **Activity Service**: `/activity-service/**`
*   **AI Service**: `/ai-service/**`

Example: To get recommendations for a user, make a GET request to:
`http://localhost:8080/ai-service/api/recommendations/user/{userId}`

## Development
*   **Lombok**: Used to reduce boilerplate code. Ensure your IDE has the Lombok plugin installed.
*   **MapStruct/ModelMapper**: Used for DTO mapping.
