# -------- Build stage: compile and package the app using Gradle (Java 17) --------
FROM gradle:8.9-jdk17 AS build            # Gradle image with JDK 17 for building
WORKDIR /home/gradle/src                  # Workdir inside the build container

# Copy the entire repository so Gradle can resolve root settings/plugins and subprojects
# This maximizes build cache reuse if your Gradle caches are mounted or populated.
COPY . .

# Build only the quote-service subproject's executable JAR.
# --no-daemon avoids keeping a background Gradle process (good for containers/CI).
RUN gradle :quote-service:clean :quote-service:bootJar --no-daemon

# -------- Run stage: run the built JAR on a lightweight JDK 17 image --------
# Using a JDK (not JRE) to ensure modules like jdk.random are present if needed.
FROM eclipse-temurin:17-jdk
WORKDIR /app

# Copy the fat JAR produced in the build stage into the runtime image
COPY --from=build /home/gradle/src/quote-service/build/libs/*.jar app.jar

# Document the container's listening port (informational; doesn't publish by itself)
EXPOSE 8081

# Health check for container orchestration (Docker/Compose/K8s) to probe liveness.
# Note: This requires curl to be available in the image. Temurin images typically
# do not include curl by default. If you see errors, either:
#  - switch to wget: `CMD wget -qO- http://localhost:8081/actuator/health >/dev/null || exit 1`
#  - or add curl in a prior layer (e.g., apt-get install -y curl) if using a Debian-based tag.
HEALTHCHECK --interval=10s --timeout=3s --start-period=15s CMD curl -f http://localhost:8081/actuator/health || exit 1

# Start the Spring Boot application
ENTRYPOINT ["java","-jar","/app/app.jar"]
