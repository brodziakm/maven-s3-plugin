pluginManagement {
  repositories {
    mavenLocal()
    gradlePluginPortal()
    mavenCentral()
    jcenter()
  }
}

rootProject.name = ("com.github.brodziakm.maven-s3")

include(":plugin")
