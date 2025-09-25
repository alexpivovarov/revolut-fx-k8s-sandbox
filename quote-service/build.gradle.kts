// Gradle plugins applied to this module
plugins {
    id("org.springframework.boot")                // Spring Boot plugin: adds tasks and packaging support
    id("io.spring.dependency-management")         // Uses Spring BOMs to align dependency versions
    id("java")                                    // Java plugin: compiles Java sources, adds test tasks
}

// Configure Java toolchain to ensure Java 17 is used for build and IDEs
java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

// Application dependencies
dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")        // Spring MVC (servlet) + embedded server
    implementation("org.springframework.boot:spring-boot-starter-actuator")   // Health/metrics/info endpoints

    // Metrics backend: exposes /actuator/prometheus when Actuator is present
    runtimeOnly("io.micrometer:micrometer-registry-prometheus")

    testImplementation("org.springframework.boot:spring-boot-starter-test")   // JUnit, AssertJ, Spring Test, Mockito

    // Tracing (OpenTelemetry via Micrometer). Send spans to an OTLP endpoint/collector.
    implementation("io.micrometer:micrometer-tracing-bridge-otel")            // Micrometer bridge to OTEL APIs
    implementation("io.opentelemetry:opentelemetry-exporter-otlp")            // OTLP exporter for traces
}

// Use JUnit Platform (Jupiter) for tests
tasks.test {
    useJUnitPlatform()
}
