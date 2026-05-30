# Architecture Overview

## Purpose

The Payment Orchestration Service acts as a central payment processing layer that routes payment requests to the appropriate payment provider while ensuring reliability, extensibility, and idempotent processing.

The application is designed using clean architecture principles with clear separation of concerns between controllers, services, routing, providers, persistence, and infrastructure components.

---

# High-Level Architecture

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

# Component Responsibilities

## PaymentController

Responsibilities:

* Exposes REST APIs
* Performs request validation
* Handles request/response mapping
* Returns standardized API responses

Endpoints:

```text
POST /payments
GET  /payments/{paymentId}
```

---

## PaymentService

Responsibilities:

* Core payment orchestration logic
* Idempotency validation
* Payment persistence
* Retry execution
* Failover execution
* Provider communication

---

## RoutingEngine

Responsibilities:

* Determine provider based on payment type
* Load routing configuration from application.yml
* Support dynamic provider mappings

Example:

```yaml
payment:
  routing:
    CARD: PROVIDER_A
    UPI: PROVIDER_B
```

---

## ProviderFactory

Responsibilities:

* Resolve provider implementation dynamically
* Decouple business logic from provider implementations

Example:

```text
PROVIDER_A → ProviderAService
PROVIDER_B → ProviderBService
```

---

## PaymentProvider

Responsibilities:

* Common abstraction for all payment providers
* Allows easy onboarding of new providers

Contract:

```java
ProviderResponse processPayment(
    CreatePaymentRequest request
);
```

---

## Redis

Responsibilities:

* Store idempotent responses
* Prevent duplicate payment processing
* Improve performance for repeated requests

---

## PostgreSQL

Responsibilities:

* Store payment transactions
* Maintain payment status history
* Persist retry information

---

# Payment Processing Flow

```text
Create Payment Request
          │
          ▼
Check Redis Cache
          │
          ├── Cached Response Found
          │         │
          │         ▼
          │     Return Response
          │
          ▼
Persist Payment
          │
          ▼
Route Provider
          │
          ▼
Process Payment
          │
          ▼
Update Payment Status
          │
          ▼
Store Response In Redis
          │
          ▼
Return Response
```

---

# Retry and Failover Flow

The system supports automatic retries and fallback provider routing.

```text
Provider A
    │
    ▼
Failure
    │
    ▼
Retry
    │
    ▼
Fallback Provider B
    │
    ▼
Success / Failure
```

Configuration:

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

# Idempotency Flow

```text
Incoming Request
       │
       ▼
Idempotency Key Present?
       │
       ▼
Check Redis
       │
       ├── Found
       │      │
       │      ▼
       │   Return Cached Response
       │
       ▼
Process Payment
       │
       ▼
Cache Response
       │
       ▼
Return Response
```

Benefits:

* Prevents duplicate payments
* Protects against client retries
* Improves reliability

---

# Design Principles

## Separation of Concerns

Each layer has a dedicated responsibility.

## Open/Closed Principle

New providers can be added without modifying existing business logic.

## Configuration Driven Design

Provider routing and failover behavior are externalized.

## Extensibility

Architecture supports:

* Additional providers
* New payment types
* Advanced routing strategies
* Event-driven integrations

---

# Future Enhancements

* Kafka-based event processing
* Async payment execution
* Distributed tracing
* Micrometer metrics
* Circuit breaker integration
* Provider health monitoring
* Multi-region deployment
* Audit logging
* OpenAPI documentation

---

# Technology Stack

| Component  | Technology         |
| ---------- | ------------------ |
| Language   | Java 21            |
| Framework  | Spring Boot        |
| Database   | PostgreSQL         |
| Cache      | Redis              |
| Build Tool | Maven              |
| Testing    | JUnit 5 + Mockito  |
| ORM        | Spring Data JPA    |
| Validation | Jakarta Validation |
