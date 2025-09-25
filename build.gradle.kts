// Root build logic for a multi-project Gradle build (Kotlin DSL)
plugins {
  id("org.springframework.boot") version "3.3.2" apply false    // Make the Boot plugin available to subprojects; don't apply at root
  id("io.spring.dependency-management") version "1.1.6" apply false // Provide Spring BOM management; subprojects opt-in
}

allprojects {
  group = "com.example"     // Maven group for all modules
  version = "0.1.0"         // Version for all modules
  repositories { mavenCentral() } // Use Maven Central for dependencies
}

subprojects {
  // Configure only subprojects that apply the Java plugin
  plugins.withId("java") {
    // Set a unified Java toolchain for compilation (JDK 17)
    extensions.configure<JavaPluginExtension> {
      toolchain.languageVersion.set(JavaLanguageVersion.of(17))
    }
    // Use JUnit Platform (Jupiter) for tests in all Java subprojects
    tasks.withType<Test>().configureEach { useJUnitPlatform() }
  }
}
