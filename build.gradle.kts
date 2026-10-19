import org.gradle.api.internal.artifacts.ivyservice.resolveengine.graph.conflicts.DefaultCapabilitiesConflictHandler.candidate
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.TestExecutable
import org.jetbrains.kotlin.gradle.targets.native.tasks.KotlinNativeTest

plugins {
  alias(libs.plugins.kotlinMultiplatform) apply false
  alias(libs.plugins.kotlinBinaryCompatibilityPlugin) apply false
  alias(libs.plugins.versionsGradlePlugin)
  alias(libs.plugins.versionCatalogUpdateGradlePlugin)
  alias(libs.plugins.dokka)
  id("com.vanniktech.maven.publish") apply false
}

repositories {
  mavenCentral()
  gradlePluginPortal()
}

buildscript {
  repositories {
    mavenCentral()
  }
}

subprojects {
  apply(plugin = rootProject.libs.plugins.dokka.get().pluginId)
  apply(plugin = rootProject.libs.plugins.kotlinBinaryCompatibilityPlugin.get().pluginId)

  plugins.withId("org.jetbrains.kotlin.multiplatform") {
    configure<KotlinMultiplatformExtension> {
      jvmToolchain(11)
      jvm()
      js(IR) {
        nodejs()
      }
      // Native targets are those published by both Arrow and the kotest Arrow extensions.
      macosX64()
      macosArm64()
      iosX64()
      iosArm64()
      iosSimulatorArm64()
      tvosX64()
      tvosArm64()
      tvosSimulatorArm64()
      watchosX64()
      watchosArm32()
      watchosArm64()
      watchosSimulatorArm64()
      linuxX64()
      mingwX64()

      // Kotest 5.9's compiler plugin cannot generate Kotlin/Native test launchers with Kotlin 2.1, so native test
      // binaries are neither built nor run. The same tests run on the JVM and JS. Remove this once the build moves to
      // Kotest 6, whose KSP based discovery supports native targets.
      targets.withType<KotlinNativeTarget>().configureEach {
        compilations.named("test") { compileTaskProvider.configure { enabled = false } }
        binaries.withType<TestExecutable>().configureEach { linkTaskProvider.configure { enabled = false } }
      }
    }
    tasks.withType<KotlinNativeTest>().configureEach { enabled = false }
  }

  tasks.withType<Test> {
    useJUnitPlatform()
    testLogging {
      events("started", "passed", "skipped", "failed")
      exceptionFormat = TestExceptionFormat.FULL
      showStandardStreams = false
    }
  }

  apply(plugin = "com.github.ben-manes.versions")

  tasks.withType<com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask> {
    revision = "release"
    resolutionStrategy {
      componentSelection {
        all {
          if (isNonStable(candidate.version) && !isNonStable(currentVersion)) {
            reject("Release candidate")
          }
        }
      }
    }
  }
}

fun isNonStable(version: String): Boolean {
  val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { version.toUpperCase().contains(it) }
  val regex = "^[0-9,.v-]+(-r)?$".toRegex()
  val isStable = stableKeyword || regex.matches(version)
  return isStable.not()
}

// this needs to be defined here for the versionCatalogUpdate
tasks.withType<com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask> {
  revision = "release"
  resolutionStrategy {
    componentSelection {
      all {
        if (isNonStable(candidate.version) && !isNonStable(currentVersion)) {
          reject("Release candidate")
        }
      }
    }
  }
}

tasks.register("publishToMavenCentral") {
  group = "publishing"
  dependsOn(
    ":lib:publishToMavenCentral",
    ":testing-lib:publishToMavenCentral",
  )
}
