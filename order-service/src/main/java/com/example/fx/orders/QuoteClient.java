package com.example.fx.orders;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker; // Opens circuit after repeated failures to prevent cascading errors
import io.github.resilience4j.retry.annotation.Retry;                  // Automatically retries failed calls (per config)

import org.springframework.core.ParameterizedTypeReference;            // Captures generic type info at runtime (Map<String, Object>)
import java.time.Duration;                                            // Duration type (note: not used directly below)
import org.springframework.stereotype.Component;                       // Marks this class as a Spring bean
import org.springframework.web.reactive.function.client.WebClient;     // Reactive non-blocking HTTP client

import java.math.BigDecimal;                                           // Precise decimal for money values
import java.time.Instant;                                              // Timestamp for fallback response
import java.util.Map;                                                  // Response payload as key/value map

@Component
public class QuoteClient {
    private final WebClient webClient;

    public QuoteClient(WebClient.Builder builder) {
        // Local development: call quote-service on your host machine, port 8081
        this.webClient = builder.baseUrl("http://localhost:8081").build();
        // In Docker/K8s: "localhost" refers to THIS container/pod. Use the service DNS name instead:
        // this.webClient = builder.baseUrl("http://quote-service:8081").build();
    }
    
    // Wrap the remote call with Resilience4j:
    // - CircuitBreaker: trips open on failures and short-circuits calls; uses "quoteService" config
    // - Retry: transparently retries transient failures with backoff (per "quoteService" config)
    @CircuitBreaker(name = "quoteService", fallbackMethod = "fallback")
    @Retry(name = "quoteService")
    public Map<String, Object> getQuote(String base, String counter, BigDecimal amount) {
        return webClient.get()
            // Build /quotes?counter=...&amount=...&base=...
            .uri(uri -> uri.path("/quotes")
                .queryParam("counter", counter)
                .queryParam("amount", amount)
                .queryParam("base", base)
                .build())
            // Perform the request and map non-2xx responses to errors
            .retrieve()
            // Decode JSON into Map<String, Object> using captured generic type
            .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
            // Block up to 3 seconds for a response (still benefits from CB/Retry)
            .block(java.time.Duration.ofSeconds(3));
    }

    // Fallback invoked when CircuitBreaker is open or retries exhaust.
    // Signature must match: original args + Throwable at the end.
    private Map<String, Object> fallback(String base, String counter, BigDecimal amount, Throwable ex) {
        return Map.of(
            "base", base,
            "counter", counter,
            "amount", amount,
            "mid", BigDecimal.ONE,         // Safe default rate when upstream is unavailable
            "ts", Instant.now().toString(),
            "fallback", true               // Signal to callers that this is a degraded response
        );
    }

}
