package com.example.fx.orders;

import org.springframework.boot.SpringApplication;                 // Boot's entrypoint utility to launch the application
import org.springframework.boot.autoconfigure.SpringBootApplication; // Enables component scanning + auto-configuration
import org.springframework.context.annotation.Bean;              // Marks a method as a Spring bean factory
import org.springframework.web.reactive.function.client.WebClient; // Reactive non-blocking HTTP client

/**
 * Main Spring Boot application for the Order Service.
 * - @SpringBootApplication triggers auto-configuration and component scanning for this package.
 */
@SpringBootApplication
public class OrderServiceApplication {

    /**
     * Main entry point. Starts the embedded server and Spring context.
     */
    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
    }

    /**
     * Exposes a WebClient.Builder bean in the application context.
     * Other components can inject WebClient.Builder and customize per call to build WebClient instances.
     */
    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

}
