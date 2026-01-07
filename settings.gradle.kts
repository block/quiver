rootProject.name = "quiver"

pluginManagement {
  repositories {
    gradlePluginPortal()
    mavenCentral()
  }
  plugins {
    id("com.vanniktech.maven.publish") version "0.33.0"
  }
}

include(":lib")
include(":testing-lib")
