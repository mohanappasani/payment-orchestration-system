# Payment Orchestration System

A simplified payment orchestration platform built using Java and Spring Boot.

This project simulates how modern payment orchestration platforms route and process payments through multiple payment providers.

---

# Features

- Create Payment API
- Fetch Payment API
- Payment Routing
- Retry & Failover
- Idempotency
- Payment Status Tracking
- Provider Abstraction
- Integration & Unit Testing

---

# Architecture

```text
Client
   ↓
Controller Layer
   ↓
Orchestration Service
   ↓
Routing Engine
   ↓
Provider Connectors
   ↓
Persistence Layer
```

---

# Tech Stack

- Java 17
- Spring Boot 3
- PostgreSQL
- Redis
- Maven
- JPA / Hibernate
- Resilience4j
- JUnit5
- Testcontainers

---

# Project Structure

```text
controller/
service/
repository/
entity/
provider/
routing/
dto/
config/
exception/
```

---

# Setup Instructions

## Clone Repository

```bash
git clone <YOUR_REPO_URL>
```

---

# Run PostgreSQL & Redis

```bash
docker-compose up
```

---

# Run Application

```bash
mvn spring-boot:run
```

---

# Run Tests

```bash
mvn test
```

---

# API Endpoints

## Create Payment

```http
POST /api/v1/payments
```

## Fetch Payment

```http
GET /api/v1/payments/{paymentId}
```

---

# Routing Logic

| Payment Type | Provider |
|---|---|
| CARD | Provider A |
| UPI | Provider B |

---

# Retry Strategy

- Retry transient failures
- Maximum 3 retries
- Backoff enabled

---

# Idempotency

This project supports idempotent payment creation using:

```http
Idempotency-Key
```

header.

Duplicate requests with the same payload return the original response.

---

# Testing

Includes:
- Unit Tests
- Integration Tests
- Negative Test Scenarios

---

# Future Improvements

- Kafka Integration
- Metrics & Monitoring
- Circuit Breaker
- Async Processing
- Distributed Tracing

---

# Author

Mohan Krishna Appasani