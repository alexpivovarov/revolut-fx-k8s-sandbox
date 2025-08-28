package com.example.fx.orders;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;

@Component
public class QuoteClient {
    private final WebClient webClient;

    public QuoteClient(WebClient.Builder builder) {
        // works both in Docker Compose (service DNS) and in Kubernetes (Service name)
        this.webClient = builder.baseUrl("http://quote-service:8081").build();
    }
    
    @CircuitBreaker(name= "quoteService", fallbackMethod = "fallback")
    @Retry(name = "quoteService")
    public Map<String, Object> getQuote(String base, String counter, BigDecimal amount) {
        return webClient.get()
            .uri(uri -> uri.path("/quotes")
            .queryParam("counter", counter)
            .queryParam("amount", amount)
            .queryParam("base", base)
            .build())
        .retrieve()
        .bodyToMono(Map.class)
        .block(); // simple sync call; CB/Retry still apply

    }

    // Fallback signature: same args + Throwable
    private Map<String,Object> fallback(String base, String counter, BigDecimal amount, Throwable ex) {
        return Map.of(
            "base", base,
            "counter", counter,
            "amount", amount,
            "mid", BigDecimal.ONE,
            "ts", Instant.now().toString(),
            "fallback", true
        );
    }

}
