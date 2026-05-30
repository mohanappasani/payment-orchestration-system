# Payment Orchestration System

A central payment processing layer built using Java and Spring Boot.

This project acts as a payment orchestration platform that routes payment requests to the appropriate payment provider while ensuring reliability, extensibility, and idempotent processing.

For detailed documentation, please refer to:
- [Requirements](docs/Requirements.md)
- [Architecture](ARCHITECTURE.md)
- [Testing Strategy](TESTING.md)

> Note: The linked docs are provided so readers can easily review the architecture and testing strategy in `ARCHITECTURE.md` and `TESTING.md`.

---

## Features

- **Create Payment API**: Exposes endpoints to create and process payments.
- **Fetch Payment API**: Retrieve payment details and status.
- **Provider Routing**: Routes payments to providers based on configurable rules (e.g., CARD to Provider A).
- **Retry Mechanism**: Automatically retries processing on provider failure (max 2 attempts).
- **Failover Routing**: Fallback to secondary providers if the primary provider fails.
- **Idempotency**: Prevents duplicate payment processing using Redis caching and an `Idempotency-Key` header.
- **Validation & Exception Handling**: Standardized error responses across all failure scenarios.

---

## Architecture Overview

```text
Client
   │
   ▼
PaymentController
   │
   ▼
PaymentService
   │
   ├──────────────► Redis (Idempotency Cache)
   │
   ▼
RoutingEngine
   │
   ▼
ProviderFactory
   │
   ▼
PaymentProvider
   │
   ▼
PostgreSQL
```

---

## Technology Stack

- **Language**: Java 21
- **Framework**: Spring Boot
- **Database**: PostgreSQL (Persistence)
- **Cache**: Redis (Idempotency & Performance)
- **Build Tool**: Maven
- **Testing**: JUnit 5 + Mockito
- **ORM**: Spring Data JPA
- **Validation**: Jakarta Validation

---

## Project Structure

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

## Setup Instructions

### Clone Repository

```bash
git clone <YOUR_REPO_URL>
```

### Run Infrastructure (PostgreSQL & Redis)

```bash
docker-compose up -d
```

### Run Application

```bash
mvn spring-boot:run
```

### Run Tests

Run all 12 automated test cases (Sanity, Regression, Negative tests):

```bash
mvn clean test
```

> Note: When you execute a Maven build such as `mvn clean install`, the test cases will also be executed as part of the build lifecycle.

---

## Configuration

The application is highly configurable via properties/yaml. Below is an example of the current configuration structure that dictates routing, provider definitions, failover setup, and simulation settings:

```yaml
payment:
  routing:
    CARD: PROVIDER_A
    UPI: PROVIDER_B
  failover:
    PROVIDER_A: PROVIDER_B
    PROVIDER_B: PROVIDER_A
  paymentTypes: [CARD, UPI]
  providerTypes: [PROVIDER_A, PROVIDER_B, PROVIDER_C]
  simulation:
    provider-a-failure-enabled: true
  retry:
    max-attempts: 2
```

---

## API Endpoints

### Create Payment

```http
POST /payments
```
**Headers:** 
- `Idempotency-Key` (required)

### Fetch Payment

```http
GET /payments/{paymentId}
```

---

## Testing

The testing suite ensures functional reliability without needing actual integration with third-party payment gateways (which are simulated).
- **Automated Tests**: Total of 12 unit/controller tests using JUnit 5 and Mockito.
- **Covered Scenarios**: Sanity Testing, Regression Testing, Negative Testing.
- **Infrastructure Validation**: Manually validated persistence (PostgreSQL) and idempotency caching (Redis).

For full details, review [TESTING.md](TESTING.md).

---

## Future Enhancements
- Kafka-based event processing
- Async payment execution
- Distributed tracing
- Micrometer metrics
- Circuit breaker integration
- Provider health monitoring
- Multi-region deployment
- Audit logging
- OpenAPI documentation

---

## Author
Mohan Krishna Appasani