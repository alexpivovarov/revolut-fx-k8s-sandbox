package com.example.fx.quotes; // Package namespace; groups related classes for the FX quotes service

import org.springframework.boot.SpringApplication; // Bootstraps and launches the Spring application
import org.springframework.boot.autoconfigure.SpringBootApplication; // Enables component scanning, autoconfiguration, and configuration

/**
 * Entry point for the FX Quote Service.
 * @SpringBootApplication combines:
 * - @Configuration: allows defining beans
 * - @EnableAutoConfiguration: configures Spring based on the classpath
 * - @ComponentScan: scans this package and subpackages for components (@RestController, @Service, etc.)
 */
@SpringBootApplication
public class QuoteServiceApplication {

    /**
     * Standard Java entry point.
     * SpringApplication.run(...) starts the embedded server (e.g., Tomcat),
     * initializes the Spring context, applies auto-configuration, and keeps the app running.
     */
    public static void main(String[] args) {
        SpringApplication.run(QuoteServiceApplication.class, args);
    }
}
