plugins {
  `java-gradle-plugin`
  `kotlin-dsl`
  `maven-publish`
  id("com.gradle.plugin-publish") version "0.12.0"
}

group = "com.github.brodziakm"
version = "0.0.2"

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

pluginBundle {
  website = "https://github.com/brodziakm/maven-s3-plugin"
  vcsUrl = "https://github.com/brodziakm/maven-s3-plugin"
  tags = listOf("aws", "awssdk", "credentials", "profile", "maven", "s3")
  description = "Supports the declaration of Maven S3 repositories that obtain their credentials from the default AWS provider chain"

  (plugins) {
    "maven-s3-plugin" {
      displayName = "Plugin for declaring Maven S3 repositories with implicit AWS credentials"
    }
  }
}


