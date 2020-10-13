plugins {
  `java-gradle-plugin`
  `kotlin-dsl`
  `maven-publish`
}

group = "com.github.brodziakm"
version = "0.0.0-SNAPSHOT"

repositories {
  mavenLocal()
  mavenCentral()
  jcenter()
}

dependencies {
  implementation("software.amazon.awssdk:auth:2.15.7")
  implementation("software.amazon.awssdk:sts:2.15.7")
}

kotlinDslPluginOptions {
  experimentalWarning.set(false)
}

gradlePlugin {
  plugins {
    create("maven-s3-plugin") {
      id = "com.github.brodziakm.maven-s3"
      implementationClass = "com.github.brodziakm.mavenS3.MavenS3Plugin"
    }
  }
}