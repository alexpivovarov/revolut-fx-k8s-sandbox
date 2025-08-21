// Root build.gradle.kts
plugins {
   // declare external plugins + versions here; don't apply at root
   id("org.springframework.boot") version "3.3.2" apply false
   id("io.spring.dependency-management") version "1.1.6" apply false
   // If you use Kotlin:
   // kotlin("jvm") version "2.0.0" apply false
   // kotlin("plugin.spring") version "2.0.0" apply false
}
allprojects {
   group = "com.example"
   version = "0.1.0"
   repositories {
       mavenCentral()
   }
}
subprojects {
   // Unify Java toolchain/bytecode across modules
   plugins.withId("java") {
       extensions.configure<JavaPluginExtension> {
           toolchain.languageVersion.set(JavaLanguageVersion.of(17))
       }
       tasks.withType<JavaCompile>().configureEach {
           options.release.set(17)
       }
       tasks.withType<Javadoc>().configureEach {
           (options as? org.gradle.external.javadoc.StandardJavadocDocletOptions)
               ?.addStringOption("Xdoclint:none", "-quiet")
       }
       configurations.all {
           // Optional: avoid caching changing modules (snapshots)
           resolutionStrategy.cacheChangingModulesFor(0, "seconds")
       }
   }
   // Common test setup (optional)
   tasks.withType<Test>().configureEach {
       useJUnitPlatform()
   }
}
// OPTIONAL: central place for shared versions
extra["springCloudVersion"] = "2023.0.3"
