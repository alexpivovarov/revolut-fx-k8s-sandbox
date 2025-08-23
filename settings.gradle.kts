plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

rootProject.name = "revolut-fx-k8s-sandbox"
include("gateway", "quote-service", "order-service")
