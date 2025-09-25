package com.example.fx.quotes;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.concurrent.ThreadLocalRandom;
import java.util.Map;
import java.math.RoundingMode;

/**
 * REST controller that exposes a simple FX quote endpoint.
 * This mock implementation returns a randomly generated mid rate
 * and echoes back the requested currency pair and amount.
 */
@RestController
@RequestMapping("/quotes")
public class QuoteController {

    /**
     * Returns a mock FX quote for the given currency pair and amount.
     *
     * Request parameters:
     * - base: ISO currency code for the base currency (e.g., "USD")
     * - counter: ISO currency code for the counter/quote currency (e.g., "EUR")
     * - amount: Optional trade amount; defaults to 1 if not provided
     *
     * Response:
     * - JSON payload containing base, counter, amount, mid (6dp), and ts (ISO-8601 timestamp)
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getQuote(
            @RequestParam String base,
            @RequestParam String counter,
            @RequestParam(required = false, defaultValue = "1") BigDecimal amount
    ) {
        // Generate a mock mid price in the range [0.5, 2.0)
        double d = ThreadLocalRandom.current().nextDouble(0.5, 2.0);

        // Convert to BigDecimal and round to 6 decimal places using HALF_UP (common for prices)
        BigDecimal mid = BigDecimal.valueOf(d).setScale(6, RoundingMode.HALF_UP);

        // Build an immutable response payload
        Map<String, Object> payload = Map.of(
            "base", base,
            "counter", counter,
            "amount", amount,
            "mid", mid,
            "ts", Instant.now().toString() // ISO-8601 timestamp
        );

        // Return HTTP 200 with the JSON payload
        return ResponseEntity.ok(payload);
    }
}
```
