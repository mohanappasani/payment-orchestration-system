# Testing Strategy

## Overview

The Payment Orchestration Service includes automated and manual testing to validate business logic, API functionality, routing behavior, retry mechanisms, failover scenarios, idempotency handling, validation rules, and exception handling.

The testing strategy is divided into the following categories:

* Sanity Testing
* Regression Testing
* Negative Testing
* Infrastructure Validation Scenarios

The goal is to ensure the application behaves reliably under both normal and failure scenarios.

---

# Automated Test Summary

| Test Suite             | Tests |
| ---------------------- | ----- |
| PaymentServiceImplTest | 4     |
| RoutingEngineTest      | 4     |
| PaymentControllerTest  | 4     |
| Total                  | 12    |

---

# 1. Sanity Tests

These tests verify that the core functionality of the application is working correctly.

| Test ID | Test Case          | Expected Result                       |
| ------- | ------------------ | ------------------------------------- |
| ST-001  | Create Payment     | Payment created successfully          |
| ST-002  | Fetch Payment      | Payment details returned successfully |
| ST-003  | Route CARD Payment | Routed to configured provider         |
| ST-004  | Route UPI Payment  | Routed to configured provider         |

---

# 2. Regression Tests

These tests ensure previously implemented functionality continues to work after enhancements and bug fixes.

| Test ID | Test Case                    | Expected Result                         |
| ------- | ---------------------------- | --------------------------------------- |
| RT-001  | Idempotency Check            | Cached response returned                |
| RT-002  | Retry Processing             | Retry executed successfully             |
| RT-003  | Provider Failover            | Fallback provider invoked               |
| RT-004  | API Response Standardization | Consistent response structure returned  |
| RT-005  | Payment Retrieval            | Existing payment retrieved successfully |

---

# 3. Negative Tests

These tests verify application behavior under invalid, unexpected, and failure conditions.

| Test ID | Test Case                      | Expected Result             |
| ------- | ------------------------------ | --------------------------- |
| NT-001  | Invalid Currency               | Validation error (400)      |
| NT-002  | Invalid Amount                 | Validation error (400)      |
| NT-003  | Missing Merchant ID            | Validation error (400)      |
| NT-004  | Missing Payment Type           | Validation error (400)      |
| NT-005  | Missing Idempotency Key        | Bad Request                 |
| NT-006  | Malformed JSON Payload         | Bad Request                 |
| NT-007  | Payment Not Found              | Not Found (404)             |
| NT-008  | Missing Provider Configuration | ProviderRoutingException    |
| NT-009  | Unsupported Payment Type       | Validation/Routing Error    |
| NT-010  | Unexpected Server Exception    | Internal Server Error (500) |

---

# Automated Test Coverage

## PaymentServiceImplTest

### createPayment_success

**Description**

Verifies successful payment creation and processing through the configured provider.

**Expected Result**

* Payment processed successfully
* Status updated correctly
* Response returned successfully

---

### createPayment_returnsCachedResponse

**Description**

Verifies duplicate requests return cached responses using the same idempotency key.

**Expected Result**

* Cached response returned
* No duplicate processing performed

---

### getPaymentById_success

**Description**

Verifies successful retrieval of payment details.

**Expected Result**

* Payment details returned successfully

---

### getPaymentById_notFound

**Description**

Verifies exception handling when payment is not found.

**Expected Result**

* PaymentNotFoundException thrown

---

## RoutingEngineTest

### routeCardToProviderA

**Description**

Verifies CARD payments are routed to Provider A.

**Expected Result**

* PROVIDER_A returned

---

### routeUpiToProviderB

**Description**

Verifies UPI payments are routed to Provider B.

**Expected Result**

* PROVIDER_B returned

---

### throwExceptionWhenPaymentTypeNotSupported

**Description**

Verifies unsupported payment types are rejected.

**Expected Result**

* ProviderRoutingException thrown

---

### getFallbackProvider

**Description**

Verifies fallback provider configuration is resolved correctly.

**Expected Result**

* Configured fallback provider returned

---

## PaymentControllerTest

### createPayment_success

**Description**

Verifies successful payment creation request.

**Expected Result**

* HTTP 201 Created
* Standardized response returned

---

### createPayment_validationFailure

**Description**

Verifies request validation handling.

**Expected Result**

* HTTP 400 Bad Request
* Validation errors returned

---

### getPayment_success

**Description**

Verifies successful payment retrieval.

**Expected Result**

* HTTP 200 OK
* Payment details returned

---

### getPayment_notFound

**Description**

Verifies payment not found handling.

**Expected Result**

* HTTP 404 Not Found
* Standardized error response returned

---

# Infrastructure Validation Scenarios

The current implementation does not include automated integration tests against third-party payment providers because provider implementations are simulated and no external payment gateway integration exists.

Infrastructure behavior was validated manually using locally running PostgreSQL and Redis instances.

## PostgreSQL Persistence Validation

### Scenario

Create a payment request.

### Expected Result

* Payment record persisted in PostgreSQL
* Payment status stored correctly
* Provider information stored correctly
* Retry count updated when retries occur

---

## Redis Idempotency Validation

### Scenario

Submit the same payment request multiple times using the same Idempotency-Key.

### Expected Result

* First request processed normally
* Response cached in Redis
* Subsequent requests return cached response
* Duplicate payment records are not created

---

## Retry Validation

### Scenario

Enable provider failure simulation and submit a payment request.

### Expected Result

* Retry mechanism executed
* Retry count updated
* Payment status updated appropriately

---

## Failover Validation

### Scenario

Primary provider fails during processing.

### Expected Result

* Fallback provider selected
* Payment processed using fallback provider
* Response returned successfully if fallback provider succeeds

---

## Routing Configuration Validation

### Scenario

Load routing configuration from application.yml.

### Expected Result

* Payment type mapped correctly
* Configured provider selected
* Routing engine resolves provider successfully

---

# Test Execution

Run all tests:

```bash
mvn test
```

Run clean build:

```bash
mvn clean test
```

Expected Result:

```text
BUILD SUCCESS
```

All 12 automated test cases should pass successfully.

---

# Quality Goals

The testing strategy validates:

* Functional correctness
* Payment routing accuracy
* Retry behavior
* Failover behavior
* Idempotency handling
* Validation rules
* Exception handling
* API response consistency
* Database persistence validation
* Redis cache validation

This testing approach helps ensure the Payment Orchestration Service remains reliable, maintainable, and production-ready.
