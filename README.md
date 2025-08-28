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


Kubernetes (Kind) — what’s set up and why

	•	We run a local single-node Kubernetes cluster with Kind (cluster name: kind-fx).

	•	Each service (quote, order, gateway) has a Deployment and a ClusterIP Service; the gateway is exposed to your machine via a NodePort (30080) so you can call /api/* from localhost.

	•	Docker images are built locally and loaded into Kind (no registry push needed), keeping the inner loop fast.

	•	Health probes (readiness/liveness) ensure pods only receive traffic when healthy and get restarted if they misbehave.

	•	Rolling updates are configured for a single-node cluster (no extra capacity), so we use a surge-free strategy to avoid Pending pods.

	•	Typical dev flow: build image → load into Kind → rollout restart → verify health, giving you repeatable, containerized deployments that mirror production patterns
Resilience patterns — what we added and how it behaves

	•	The order service calls the quote service through a small client that applies Resilience4j policies.

	•	Circuit Breaker: watches recent calls (count-based window). If ~≥50% fail, it opens for ~10s to stop hammering a bad dependency; later it half-opens with a few trial calls before closing.

	•	Retry: transient network errors are retried a few times with a short back-off to smooth over brief glitches.

	•	Fallback: if the breaker is open (or calls still fail), the client returns a safe, deterministic quote payload (marked as a fallback) so the order flow degrades gracefully instead of hard-failing.

	•	Observability: Spring Boot Actuator exposes health that includes the breaker status, plus Micrometer/Prometheus metrics for breaker and retry outcomes.

	•	Idempotency: the order API accepts X-Idempotency-Key so repeating the same request won’t create duplicate orders.
 
