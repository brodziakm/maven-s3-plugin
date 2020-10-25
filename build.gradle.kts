plugins {
  `java-gradle-plugin`
  `kotlin-dsl`
  `maven-publish`
  id("com.gradle.plugin-publish") version "0.12.0"
}

group = "com.github.brodziakm"
version = "1.3.0"

repositories {
  mavenLocal()
  mavenCentral()
  jcenter()
}

dependencies {
  implementation("software.amazon.awssdk:auth:2.15.14")
  implementation("software.amazon.awssdk:sts:2.15.14")
}

kotlinDslPluginOptions {
  experimentalWarning.set(false)
}

configure<JavaPluginConvention> {
  sourceCompatibility = JavaVersion.VERSION_1_8
  targetCompatibility = JavaVersion.VERSION_1_8
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
  description = "Supports the binding of AWS credentials (obtained via the AWS SDK default provider chain) to Maven S3 repositories"

  (plugins) {
    "maven-s3-plugin" {
      displayName = "Plugin for binding AWS credentials to Maven S3 repositories"
    }
  }
}


