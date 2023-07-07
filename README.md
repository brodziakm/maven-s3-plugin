# maven-s3-plugin

A simple Gradle plugin that provides a configurable action for binding Gradle AWS credentials using the default 
AWS credential provider chain. It also allows the optional specification of a profile. 

Unlike Gradle's in-built AwsImAuthentication, this can be leveraged in scenarios that require role switching, OIDC SSO,
or other more advanced authentication scenarios that are natively supported by the AWS SDK.

## Features

Supports:
* The AWS Java SDK 2.x default provider chain credential lookup, along with all capabilities that it provides
* Single Sign On via OIDC, if configured (see https://docs.aws.amazon.com/cli/latest/userguide/sso-configure-profile-token.html)
* Optional specification of AWS profile names
* Compatible with Gradle 6.6

## Why does this plugin exist?

Gradle has an in-built mechanism for authenticating against AWS for access to S3 (see [Declaring an S3 backed Maven and Ivy repository](https://docs.gradle.org/current/userguide/declaring_repositories.html#ex-declaring-an-s3-backed-maven-and-ivy-repository)). 
This mechanism does not work for many of the authentication scenarios that the AWS SDK supports. It populates an 
instance of [AwsCredentials](https://docs.gradle.org/current/javadoc/org/gradle/api/credentials/AwsCredentials.html) via [AwsImAuthentication](https://docs.gradle.org/current/javadoc/org/gradle/authentication/aws/AwsImAuthentication.html), which
does not work in many authentication scenarios (or, at least the ones that I have been working with).

This plugin exists to allow authentication via Gradle's maven repository configuration, where the repository is an S3
bucket that requires AWS authentication. All the plugin does is to delegate the credential acquisition process to the
AWS SDK via [DefaultCredentialsProvider](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/auth/credentials/DefaultCredentialsProvider.html),
and then pass these back via [AwsCredentials](https://docs.gradle.org/current/javadoc/org/gradle/api/credentials/AwsCredentials.html).

This plugin supports all capabilities that are provided from the AWS SDK default credentials provider chain as of the
**AWS SDK version 2.20.99**.

## How to use

#### Import the plugin

In Kotlin `build.gradle.kts`

```kotlin
plugins {
  id("com.github.brodziakm.maven-s3") version "1.4.0"
}
```

In Groovy `build.gradle`

```groovy
plugins {
  id "com.github.brodziakm.maven-s3" version "1.4.0"
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

As per [Supplying and Retrieving AWS Credentials](https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/credentials-chain.html)

For single sign on, see [Token provider configuration with automatic authentication refresh for AWS IAM Identity Center](https://docs.aws.amazon.com/cli/latest/userguide/sso-configure-profile-token.html)

## Acknowledgements

This is inspired by the [awsm-credentials-gradle](https://github.com/itsallcode/awsm-credentials-gradle) plugin. This
plugin sadly no longer works on Gradle 6.6+ due to changes in the repository credentials API. 

## Contributing

Feel free to open a issue or submit a pull request for any bugs/improvements.
