import org.jetbrains.dokka.base.DokkaBaseConfiguration
import org.jetbrains.dokka.gradle.DokkaTask
import java.net.URL

plugins {
  alias(libs.plugins.kotlinMultiplatform)
  alias(libs.plugins.ksp)
  alias(libs.plugins.kotest)
  alias(libs.plugins.dokka)
  id("com.vanniktech.maven.publish")
}

repositories {
  mavenCentral()
}

buildscript {
  dependencies {
    classpath(libs.plugins.dokkaBase.get().toString())
  }
}

mavenPublishing {
  configure(com.vanniktech.maven.publish.KotlinMultiplatform())
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      implementation(libs.arrowCore)
      implementation(libs.arrowResilience)
    }

    commonTest.dependencies {
      implementation(project(":testing-lib"))
      implementation(libs.kotestAssertions)
      implementation(libs.kotestAssertionsArrow)
      implementation(libs.kotestFrameworkEngine)
      implementation(libs.kotestProperty)
      implementation(libs.kotestPropertyArrow)
    }

    jvmTest.dependencies {
      implementation(libs.kotestJunitRunnerJvm)
    }
  }
}

// Copies Quiver logo into Dokka output directory, making images accessible in documentation
tasks.register<Copy>("copyDocumentationImages") {
  from("../images/quiver-logo-01.svg", "../images/quiver-logo-02.svg")
  into("${getRootDir()}/lib/build/dokka/html/doc-images")
}

tasks.withType<DokkaTask>().configureEach {
  dependsOn("copyDocumentationImages")
  pluginConfiguration<org.jetbrains.dokka.base.DokkaBase, DokkaBaseConfiguration> {
    customStyleSheets = listOf(file("custom-styles.css"))
  }

  dokkaSourceSets {
    named("commonMain") {
      moduleName.set("Quiver Library")

      // Includes custom documentation
      includes.from("module.md")

      // Points source links to GitHub
      sourceLink {
        localDirectory.set(file("src/commonMain/kotlin"))
        remoteUrl.set(URL("https://github.com/cashapp/quiver/tree/master/lib/src/commonMain/kotlin"))
        remoteLineSuffix.set("#L")
      }
    }
  }
}
