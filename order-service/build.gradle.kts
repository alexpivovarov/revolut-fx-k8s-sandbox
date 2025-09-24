// Gradle plugins used by this project
plugins {
    id("org.springframework.boot")                 // Spring Boot plugin: packaging, tasks, dependency management conveniences
    id("io.spring.dependency-management")          // Aligns dependency versions via Spring's BOMs
    id("java")                                     // Java plugin: compiles Java, adds standard tasks
}

// Application dependencies
dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")        // Spring MVC (Servlet-based)
    implementation("org.springframework.boot:spring-boot-starter-webflux")    // Reactive Web (Netty by default)
    // or swap for the leaner reactive core without Boot auto-config:
    // implementation("org.springframework:spring-webflux")
    implementation("org.springframework.boot:spring-boot-starter-actuator")   // Health, metrics, info, management endpoints

    // Tracing setup (OpenTelemetry via Micrometer). Keep if you export traces to an OTEL collector.
    // Otherwise disable tracing in application properties to avoid overhead.
    implementation("io.micrometer:micrometer-tracing-bridge-otel")            // Micrometer bridge to OpenTelemetry API
    implementation("io.opentelemetry:opentelemetry-exporter-otlp")            // Exports traces to OTLP endpoint

    // Resilience4j for fault tolerance patterns (CircuitBreaker, RateLimiter, TimeLimiter, etc.)
    implementation("org.springframework.boot:spring-boot-starter-aop")        // Enables @Aspect-based annotations used by Resilience4j
    implementation("io.github.resilience4j:resilience4j-spring-boot3:2.2.0")  // Boot 3 autoconfiguration + annotations
    implementation("io.github.resilience4j:resilience4j-timelimiter:2.2.0")   // TimeLimiter support
    // Optional Reactor operators if using WebFlux with reactive Resilience4j:
    // implementation("io.github.resilience4j:resilience4j-reactor:2.2.0")

    // Metrics backend: expose metrics in Prometheus format at /actuator/prometheus
    runtimeOnly("io.micrometer:micrometer-registry-prometheus")

    // Test dependencies (includes JUnit, Mockito, Spring Test slices)
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

// Configure the test task to use JUnit Platform (JUnit Jupiter)
tasks.test {
    useJUnitPlatform()
}
