# Distributed Patient Management System

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.0-green.svg)](https://spring.io/projects/spring-boot)
[![Docker](https://img.shields.io/badge/Docker-Enabled-blue.svg)](https://www.docker.com/)
[![LocalStack](https://img.shields.io/badge/AWS-LocalStack-purple.svg)](https://localstack.cloud/)

A scalable, fault-tolerant healthcare microservices platform designed to handle patient data management, billing processing, and real-time analytics. 

This project simulates a cloud-native AWS architecture locally using **LocalStack**, demonstrating a hybrid communication pattern using **gRPC** (synchronous) and **Kafka** (asynchronous).

---

## System Architecture

The system mimics a production-grade **AWS ECS** deployment but is orchestrated locally via Docker and LocalStack to simulate cloud services (ALB, RDS, MSK).


### Service Breakdown
* **API Gateway:** The entry point for all client requests, routing traffic to appropriate microservices.
* **Auth Service:** Handles user authentication and issues JWT tokens for secure access.
* **Patient Service (Core):** Manages patient records. Acts as a **gRPC Client** to the Billing Service and a **Kafka Producer** for the Analytics Service.
* **Billing Service:** A low-latency service accessible via **gRPC** for processing payments and insurance claims.
* **Analytics Service:** Consumes events asynchronously from **Kafka** to generate insights on patient demographics and system usage.

---

## Tech Stack

- **Language:** Java 17
- **Framework:** Spring Boot (Web, Data JPA, Security)
- **Communication:**
  - **Synchronous:** gRPC (Protobuf) for inter-service communication (Patient $\leftrightarrow$ Billing).
  - **Asynchronous:** Apache Kafka for event-driven architecture.
- **Database:** PostgreSQL (RDS simulation).
- **Infrastructure & DevOps:**
  - **Docker & Docker Compose:** Containerization and orchestration.
  - **LocalStack:** Simulating AWS services (S3, SQS, etc.) locally.
  - **Nginx/ALB:** Load balancing.

---

## Key Features

* **Hybrid Communication Patterns:**
    * Utilizes **gRPC** for high-performance, strict-contract synchronous communication between critical services (Patient & Billing).
    * Implements **Event-Driven Architecture** using Kafka to decouple the Analytics service from the main user flow, ensuring high availability.
* **Cloud-Native Simulation:**
    * Full AWS environment simulation using **LocalStack** to replicate a VPC, Private Subnets, and ECS Tasks structure on a local machine.
* **Centralized Gateway:**
    * Unified entry point for routing, simplifying client-side consumption.
* **Data Consistency:**
    * Transactional integrity within services with eventual consistency for analytics data.

---

## Getting Started

### Prerequisites
* Docker & Docker Compose
* Java 17+
* Maven

### Installation & Running

1.  **Clone the repository**
    ```bash
    git clone [https://github.com/yourusername/patient-management-system.git](https://github.com/yourusername/patient-management-system.git)
    cd patient-management-system
    ```

2.  **Build the Microservices**
    ```bash
    mvn clean package -DskipTests
    ```

3.  **Start the Infrastructure (LocalStack & Databases)**
    ```bash
    # This starts Zookeeper, Kafka, Postgres, and LocalStack
    docker-compose up -d
    ```

4.  **Verify Services**
    * API Gateway: `http://localhost:8080`
    * Eureka/Consul (if used): `http://localhost:8761`
    * LocalStack Dashboard: `http://localhost:4566`

---

## API Usage Examples

### 1. Create a Patient (Triggers Kafka Event)
**POST** `/api/patients`
```json
{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john@example.com"
}

```

*Result:* Saves to DB -> Sends `PATIENT_CREATED` event to Kafka -> Consumed by Analytics Service.

### 2. Get Patient Bill (Uses gRPC)

**GET** `/api/patients/{id}/bill`
*Result:* Patient Service makes a gRPC call to Billing Service to retrieve real-time balance.

---

## Future Improvements

* Implement **ELK Stack** (Elasticsearch, Logstash, Kibana) for centralized logging.
* Add **Resilience4j** for Circuit Breaking and Retry patterns.
* Deploy to a real AWS Free Tier account using Terraform.

---
