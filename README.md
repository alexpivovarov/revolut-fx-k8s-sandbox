# Real-Time FX & Payments Sandbox (Kubernetes-ready)

A compact fintech-style system with three Spring Boot services deployed via Helm on Kubernetes.

## Services
- **gateway** — Spring Cloud Gateway entrypoint (`/api/**`).
- **quote-service** — returns mock FX (foreign exchange) quotes (`/quotes?base=USD&counter=EUR`).
- **order-service** — creates mock orders with idempotency header support (`X-Idempotency-Key`).

## Run locally
```bash
./gradlew :quote-service:bootRun
./gradlew :order-service:bootRun
./gradlew :gateway:bootRun
```
