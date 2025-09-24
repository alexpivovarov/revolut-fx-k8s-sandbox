# -------- Build stage: compile and package the app with Gradle --------
FROM gradle:8.9-jdk17 AS build
WORKDIR /home/gradle/src
# Copy the whole repo so Gradle can resolve root settings/plugins and subprojects
COPY . .
# Build only the order-service subproject JAR (skips tests by default unless configured)
RUN gradle :order-service:clean :order-service:bootJar --no-daemon

# -------- Run stage: lightweight JRE image to run the built JAR --------
FROM eclipse-temurin:21-jre
WORKDIR /app
# Copy the fat JAR produced in the build stage into the runtime image
COPY --from=build /home/gradle/src/order-service/build/libs/*.jar app.jar

# Document the container's listening port
EXPOSE 8082

# Container health check (ensure your image has 'curl' installed or switch to wget/busybox)
HEALTHCHECK --interval=10s --timeout=3s --start-period=15s CMD curl -f http://localhost:8082/actuator/health || exit 1

# Start the Spring Boot application
ENTRYPOINT ["java","-jar","/app/app.jar"]
