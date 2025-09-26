// This file defines the shape of the build (root name + included modules)
// and how Gradle resolves plugins. It executes before any project is configured.

plugins {
    // Applies Foojay's toolchains resolver convention plugin.
    // Purpose: Automatically discover and provision the correct JDK toolchains
    // (via the Foojay Discovery API) so all developers/CI use consistent Java versions.
    // This reduces environment drift and "wrong JDK" build failures.
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

pluginManagement {
    repositories {
        // Repositories used specifically for resolving Gradle plugins declared in `plugins {}` blocks.
        // Note: This does NOT affect dependency resolution for subprojects; that is configured
        // in regular build scripts (e.g., build.gradle.kts) under `repositories`.
        mavenCentral()
        gradlePluginPortal() // The official Gradle plugin repository
    }
}

// Sets the logical name of the root project. Affects IDE import and can influence artifact coordinates.
rootProject.name = "revolut-fx-k8s-sandbox"

// Declares a multi-project build with three modules. Gradle expects matching directories at the root:
//   ./gateway, ./quote-service, ./order-service
// Each module should have its own build script and can have its own dependencies and plugins.
include("gateway", "quote-service", "order-service")
