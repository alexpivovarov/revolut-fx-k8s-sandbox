plugins {
    // Spring Boot Gradle plugin (packaging, bootRun, bootJar)
    id("org.springframework.boot")
    // Aligns dependency versions via BOMs
    id("io.spring.dependency-management")
    // Java plugin (compilation tasks, test tasks, jar, etc.)
    id("java")
}

dependencies {
    // Actuator endpoints (/actuator/*) for health/metrics/info
    implementation("org.springframework.boot:spring-boot-starter-actuator")

    // Spring Cloud Gateway (reactive reverse proxy on Netty)
    implementation("org.springframework.cloud:spring-cloud-starter-gateway")

    // Enable Prometheus scrape endpoint at /actuator/prometheus
    runtimeOnly("io.micrometer:micrometer-registry-prometheus")

    // Test framework (JUnit + Spring test utilities)
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    // ---- Tracing (OpenTelemetry via Micrometer) ----
    // Bridge from Micrometer tracing API to OpenTelemetry
    implementation("io.micrometer:micrometer-tracing-bridge-otel")
    // Exporter that ships traces to an OTEL collector (OTLP)
    implementation("io.opentelemetry:opentelemetry-exporter-otlp")
}

dependencyManagement {
    imports {
        // Spring Cloud BOM to keep gateway and related deps in compatible versions
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:2023.0.3")
    }
}

tasks.test {
    // Run tests on JUnit Platform (JUnit 5)
    useJUnitPlatform()
}
