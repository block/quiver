plugins {
  alias(libs.plugins.kotlinMultiplatform)
  id("com.vanniktech.maven.publish")
}

repositories {
  mavenCentral()
}

mavenPublishing {
  configure(com.vanniktech.maven.publish.KotlinMultiplatform())
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      implementation(project(":lib"))
      implementation(libs.arrowCore)
      implementation(libs.kotestAssertions)
      implementation(libs.kotestProperty)
      implementation(libs.kotestPropertyArrow)
    }
  }
}
