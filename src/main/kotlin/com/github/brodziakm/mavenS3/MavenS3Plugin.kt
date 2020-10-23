package com.github.brodziakm.mavenS3

import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.credentials.AwsCredentials
import software.amazon.awssdk.auth.credentials.AwsSessionCredentials
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider


/**
 * A Gradle extension that allows AWS credentials to be resolved using the AWS default credential provider chain.
 * This approach is compatible with Gradle 6.6, which has additional checks that require credentials to exist when the
 * repository is defined.
 */
open class MavenS3Extension(private val project: Project) {

  /**
   * Resolve AWS credentials into the provided AwsCredentials instance using the default provider chain
   */
  fun resolve(credentials: AwsCredentials) {
    resolve(credentials, null)
  }

  /**
   * Resolve AWS credentials into the provided AwsCredentials instance using the default provider chain, and explicitly
   * specifying the provided role.
   */
  fun resolve(credentials: AwsCredentials, profile: String?) {
    val action = AwsCredentialsAction(credentialsProvider(profile))
    action.execute(credentials)
  }

  /**
   * Get a default credentials provider, configured to use the provided profile if specified
   */
  private fun credentialsProvider(profile: String?): DefaultCredentialsProvider {
    if (profile != null) {
      project.logger.info("Using AWS profile '${profile}'")
      return DefaultCredentialsProvider.builder().profileName(profile).build()
    }
    return DefaultCredentialsProvider.create()
  }

  /**
   * A simple action that applies credentials obtained via the default AWS provider chain to the Gradle AWS credentials
   */
  inner class AwsCredentialsAction(private val provider: DefaultCredentialsProvider) : Action<AwsCredentials> {
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
