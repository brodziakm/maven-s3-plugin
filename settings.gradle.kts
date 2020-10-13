pluginManagement {
  repositories {
    gradlePluginPortal()
    mavenCentral()
    jcenter()
  }
}

rootProject.name = ("maven-s3-plugin")

include(":example")
includeBuild("plugin-build")
