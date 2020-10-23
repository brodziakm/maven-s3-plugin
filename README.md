# maven-s3-plugin

A simple Gradle plugin that exposes a 'mavenS3' repository type. This is just a normal 'maven' repository that
delegates credential acquisition to the AWS SDK. It also allows the optional specification of a profile.

## Features

Supports:
* Default AWS provider chain credential lookup
* Role switching (via AWS profiles)
* Optional specification of AWS profile names
* Compatible with Gradle 6.6
  
## How to use

#### Import the plugin

In Kotlin `build.gradle.kts`

```kotlin
plugins {
  id("com.github.brodziakm.maven-s3") version "1.2.0"
}
```

In Groovy `build.gradle`

```groovy
plugins {
  id "com.github.brodziakm.maven-s3" version "1.2.0"
}
```

#### Declare your repositories

In Kotlin `build.gradle.kts`

```kotlin
repositories {
  mavenCentral() // or any as required per normal DSL
  maven {
    url = uri("<your S3 url here>")
      credentials(AwsCredentials::class) {
        mavenS3.resolve(this)
      }
    }
  }
  maven {
    url = uri("<your S3 url here>")
      credentials(AwsCredentials::class) {
        mavenS3.resolve(this, "<your profile name here>") // optionally specify a profile name
      }
    }
  }
}
```

#### Specify your AWS credentials

As per [Supplying and Retrieving AWS Credentials](https://docs.aws.amazon.com/sdk-for-java/v2/developer-guide/credentials.html)



## Acknowledgements

This is inspired by the [awsm-credentials-gradle](https://github.com/itsallcode/awsm-credentials-gradle) plugin. This
plugin sadly no longer works on Gradle 6.6+ due to changes in the repository credentials API. 

## Contributing

Feel free to open a issue or submit a pull request for any bugs/improvements.
