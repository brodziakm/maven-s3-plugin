plugins {
  `java-gradle-plugin`
  `kotlin-dsl`
  `maven-publish`
  id("com.gradle.plugin-publish") version "1.3.1"
}

group = "com.github.brodziakm"
version = "1.5.0"

repositories {
  mavenLocal()
  mavenCentral()
}

extra.apply {
  set("awssdk.version", "2.31.62")
}

dependencies {
  implementation("software.amazon.awssdk:auth:${property("awssdk.version")}")
  implementation("software.amazon.awssdk:sts:${property("awssdk.version")}")
  implementation("software.amazon.awssdk:sso:${property("awssdk.version")}")
  runtimeOnly("software.amazon.awssdk:ssooidc:${property("awssdk.version")}")
}

java {
  toolchain {
    languageVersion.set(JavaLanguageVersion.of(21))
  }

  sourceCompatibility = JavaVersion.toVersion("21")
  targetCompatibility = JavaVersion.toVersion("21")
}

gradlePlugin {
  website.set("https://github.com/brodziakm/maven-s3-plugin")
  vcsUrl.set("https://github.com/brodziakm/maven-s3-plugin")
  plugins {
    create("maven-s3-plugin") {
      id = "com.github.brodziakm.maven-s3"
      displayName = "Maven S3 Plugin"
      description = "Leverages the AWS SDK for Java 2.x via its default provider chain to authenticate access to S3 for use as a Maven repository. Supports OIDC SSO."
      tags = listOf("aws", "awssdk", "credentials", "profile", "sso", "oidc", "maven", "s3")
      implementationClass = "com.github.brodziakm.mavenS3.MavenS3Plugin"
    }
  }
}
