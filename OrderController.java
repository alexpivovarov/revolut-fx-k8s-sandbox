package com.example.fx.orders;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@RestController
@RequestMapping("/orders")
public class OrderController {

    private final Map<String, Map<String, Object>> idempotencyStore = new ConcurrentHashMap<>();
    private final QuoteClient quoteClient;

    public OrderController(QuoteClient quoteClient) {
        this.quoteClient = quoteClient;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createOrder(
            @RequestHeader(name = "X-Idempotency-Key", required = false) String idemKey,
            @RequestBody OrderRequest req
    ) {
        // Check idempotency
        if (idemKey != null && idempotencyStore.containsKey(idemKey)) {
            return ResponseEntity.status(HttpStatus.OK).body(idempotencyStore.get(idemKey));
        }


        // Call quote-service via resilient client
        Map<String, Object> quoteResponse = quoteClient.getQuote(req.base(), req.counter(), req.amount());

        String id = "ord_" + Long.toHexString(System.nanoTime());
        Map<String, Object> payload = Map.of(
                "id", id,
                "base", req.base(),
                "counter", req.counter(),
                "amount", req.amount(),
                "createdAt", Instant.now().toString(),
                "status", "CREATED",
                "rate", quoteResponse.get("mid")
        );

        // Save for idempotency
        if (idemKey != null) {
            idempotencyStore.put(idemKey, payload);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(payload);
    }

    public record OrderRequest(String base, String counter, BigDecimal amount) {}
}
