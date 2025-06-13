package com.github.brodziakm.mavenS3

import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.credentials.AwsCredentials
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider
import software.amazon.awssdk.auth.credentials.AwsSessionCredentials
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider
import com.github.brodziakm.aws.AwsProfileCredentialsProvider


/**
 * A Gradle extension that allows AWS credentials to be resolved using the AWS default credential provider chain.
 * This approach is compatible with Gradle 6.6, which has additional checks that require credentials to exist when the
 * repository is defined.
 */
open class MavenS3Extension(private val project: Project) {

  /**
   * Generate an action that applies AWS credentials from the default provider chain to the Gradle AwsCredentials
   * context.
   */
  fun credentials() : Action<AwsCredentials> {
    return credentials(System.getenv("MAVEN_S3_AWS_PROFILE"))
  }

  /**
   * Generate an action that applies AWS credentials from the default provider chain, using the specified role, to the
   * Gradle AwsCredentials context.
   */
  fun credentials(profile: String?) : Action<AwsCredentials> {
    return AwsCredentialsAction(credentialsProvider(profile))
  }

  /**
   * Get a default credentials provider, configured to use the provided profile if specified
   */
  private fun credentialsProvider(profile: String?): AwsCredentialsProvider {
    if (profile != null) {
      project.logger.info("Using AWS profile '${profile}'")
      return AwsProfileCredentialsProvider(profile)
    }
    return DefaultCredentialsProvider.builder().build()
  }

  /**
   * A simple action that applies credentials obtained via the default AWS provider chain to the Gradle AWS credentials
   */
  inner class AwsCredentialsAction(private val provider: AwsCredentialsProvider) : Action<AwsCredentials> {
    override fun execute(credentials: AwsCredentials) {
      val awsCredentials = provider.resolveCredentials()
      credentials.accessKey = awsCredentials.accessKeyId()
      credentials.secretKey = awsCredentials.secretAccessKey()
      credentials.sessionToken = sessionToken(awsCredentials)
    }

    private fun sessionToken(credentials: software.amazon.awssdk.auth.credentials.AwsCredentials): String? {
      if (credentials is AwsSessionCredentials) {
        project.logger.info("AWS session established")
        return credentials.sessionToken()
      }
      project.logger.info("No AWS session, using IAM credentials")
      return null
    }
  }
}

class MavenS3Plugin : Plugin<Project> {
  override fun apply(project: Project) {
    project.logger.info("Maven S3 plugin is active")
    project.extensions.create("mavenS3", MavenS3Extension::class.java, project)
  }
}
