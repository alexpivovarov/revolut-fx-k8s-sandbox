package com.example.fx.orders;

import org.springframework.http.HttpStatus;                  // HTTP status code constants (e.g., 200, 201)
import org.springframework.http.ResponseEntity;           // Wrapper for HTTP responses with body/status/headers
import org.springframework.web.bind.annotation.*;         // Spring MVC annotations (@RestController, @RequestMapping, etc.)

import java.math.BigDecimal;                              // Arbitrary-precision decimal for currency amounts
import java.time.Instant;                                 // Machine timestamp in UTC (ISO-8601)
import java.util.Map;                                     // Key-value map interface
import java.util.concurrent.ConcurrentHashMap;            // Thread-safe map for concurrent access

/**
 * REST controller for creating FX orders.
 * Exposes endpoints under /orders.
 */
@RestController
@RequestMapping("/orders")
public class OrderController {

    // In-memory store to implement idempotency using the X-Idempotency-Key header.
    // Key: idempotency key; Value: previously created order response payload.
    private final Map<String, Map<String, Object>> idempotencyStore = new ConcurrentHashMap<>();

    // Client used to call the quote-service to fetch FX rates.
    private final QuoteClient quoteClient;

    // Spring injects QuoteClient (e.g., via @Bean or @Component).
    public OrderController(QuoteClient quoteClient) {
        this.quoteClient = quoteClient;
    }

    /**
     * Create a new order.
     *
     * - If "X-Idempotency-Key" is provided and seen before, returns the same response (HTTP 200).
     * - Otherwise calls quote-service, creates an order, and returns HTTP 201 with the new payload.
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createOrder(
            @RequestHeader(name = "X-Idempotency-Key", required = false) String idemKey,
            @RequestBody OrderRequest req
    ) {
        // Idempotency check: if the same key was used before, return the cached response.
        if (idemKey != null && idempotencyStore.containsKey(idemKey)) {
            return ResponseEntity.status(HttpStatus.OK).body(idempotencyStore.get(idemKey));
        }

        // Query the quote-service for the mid rate for the requested currency pair and amount.
        Map<String, Object> quoteResponse = quoteClient.getQuote(req.base(), req.counter(), req.amount());

        // Generate a simple unique order ID (nanotime-based; sufficient for demo purposes).
        String id = "ord_" + Long.toHexString(System.nanoTime());

        // Build the response payload returned to the client and (optionally) cached for idempotency.
        Map<String, Object> payload = Map.of(
                "id", id,
                "base", req.base(),
                "counter", req.counter(),
                "amount", req.amount(),
                "createdAt", Instant.now().toString(),
                "status", "CREATED",
                "rate", quoteResponse.get("mid") // mid-market rate from quote-service
        );

        // Persist the payload under the idempotency key, so retries return the same response.
        if (idemKey != null) {
            idempotencyStore.put(idemKey, payload);
        }

        // New resource created → return 201 Created with the order payload.
        return ResponseEntity.status(HttpStatus.CREATED).body(payload);
    }

    /**
     * Request body for creating orders.
     * Example:
     * {
     *   "base": "EUR",
     *   "counter": "USD",
     *   "amount": 123.45
     * }
     */
    public record OrderRequest(String base, String counter, BigDecimal amount) {}
}
