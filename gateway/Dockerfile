# ---------- Build stage: compile the app and create the Boot JAR ----------
FROM gradle:8.9-jdk17 AS build           # Gradle + JDK 17 image for building
WORKDIR /home/gradle/src                 # workdir inside the container

COPY . .                                  # copy the whole repo into the build image
# Build only the gateway module’s fat jar (includes deps)
RUN gradle :gateway:clean :gateway:bootJar --no-daemon



# ---------- Run stage: small runtime image to run the jar ----------
FROM eclipse-temurin:17-jre              # Slim JRE 17 runtime (no compiler)
WORKDIR /app

# Copy the jar produced in the build stage
COPY --from=build /home/gradle/src/gateway/build/libs/*.jar app.jar

EXPOSE 8080                               # document the port (tools/readers)
# Health checks are usually done by Kubernetes (readiness/liveness probes)
ENTRYPOINT ["java","-jar","/app/app.jar"] # start Spring Boot
