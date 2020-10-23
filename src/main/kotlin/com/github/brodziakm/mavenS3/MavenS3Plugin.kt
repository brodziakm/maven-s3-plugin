package com.github.brodziakm.mavenS3

import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider
import software.amazon.awssdk.auth.credentials.AwsSessionCredentials
import java.net.URI
import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.artifacts.repositories.MavenArtifactRepository
import org.gradle.api.credentials.AwsCredentials


/**
 * A Gradle extension that exposes the 'mavenS3' repository type to Gradle build scripts. Underneath it all this is
 * just an ordinary Maven repository (albeit with an S3 url), but the repository obtains it's credentials via actions
 * that delegate to the AWS SDK and the default credential provider chain. This approach is compatible with Gradle 6.6,
 * which has additional checks that require credentials to exist when the repository is defined.
 */
open class MavenS3Extension(private val project: Project) {

  /**
   * @param url the url of the s3 bucket holding the Maven repository
   */
  fun at(url: String) : MavenArtifactRepository {
    return at(url, null)
  }

  /**
   * @param url the url of the s3 bucket holding the Maven repository
   * @param profile the optional profile name to use when resolving credentials
   */
  fun at(url: String, profile: String?) : MavenArtifactRepository {
    return project.repositories.maven(S3Action(url, profile))
  }

  /**
   * An action that applies URL and credentials to a Maven artifact repository, but does so using an action that obtains
   * credentials from the AWS provider chain.
   */
  inner class S3Action(private val url: String, profile: String?) : Action<MavenArtifactRepository> {

    private val provider = credentialsProvider(profile)

    override fun execute(repository: MavenArtifactRepository) {
      repository.name = "MavenS3"
      repository.url = URI(url)
      repository.credentials(AwsCredentials::class.java, AwsCredentialsAction(provider))
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
