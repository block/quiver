plugins {
  id("java-library")
  id("org.jetbrains.kotlin.jvm")
}

repositories {
  mavenCentral()
}

sourceSets {
  val test by getting {
    java.srcDir("src/test/kotlin/")
  }
}

java {
  toolchain {
    languageVersion.set(JavaLanguageVersion.of(11))
  }
  withSourcesJar()
}

dependencies {
  implementation(project(":lib"))
  implementation(libs.arrowCore)
  implementation(libs.kotestProperty)
  implementation(libs.kotestPropertyArrow)

  testImplementation(libs.junitApi)
  testImplementation(libs.kotestAssertions)
  testImplementation(libs.kotestAssertionsArrow)
  testImplementation(libs.kotestFrameworkApi)
  testImplementation(libs.kotestJunitRunnerJvm)

  testRuntimeOnly(libs.junitEngine)
}
