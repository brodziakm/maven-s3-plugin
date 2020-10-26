# maven-s3-plugin

A simple Gradle plugin that provides a configurable action for binding Gradle AWS credentials using the default 
AWS credential provider chain. It also allows the optional specification of a profile. 

Unlike Gradle's in-built AwsImAuthentication, this can be leveraged in scenarios that require role switching. 

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
  id("com.github.brodziakm.maven-s3") version "1.3.0"
}
```

In Groovy `build.gradle`

```groovy
plugins {
  id "com.github.brodziakm.maven-s3" version "1.3.0"
}
```

#### Declare your repositories

In Kotlin `build.gradle.kts`

```kotlin
repositories {
  mavenCentral() // or any as required per normal DSL
  // option without specific profile:
  maven {
    url = uri("<your S3 url here>")
    credentials(AwsCredentials::class, mavenS3.credentials())
  }
  // option with specific profile:
  maven {
    url = uri("<your S3 url here>")
    credentials(AwsCredentials::class, mavenS3.credentials("<your profile name here>"))
  }
}
```

In Groovy `build.gradle`

```groovy
repositories {
  // ... other repository definitions as required
  // option without specific profile:
  maven {
    url '<your repository url here>'
    credentials(AwsCredentials, mavenS3.credentials())
  }
  // option with specific profile:
  maven {
    url '<your repository url here>'
    credentials(AwsCredentials, mavenS3.credentials('<your profile name here>'))
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
