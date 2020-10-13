object PluginCoordinates {
  const val ID = "com.github.brodziakm.maven-s3.plugin"
  const val GROUP = "com.github.brodziakm.maven-s3"
  const val VERSION = "0.0.1"
  const val IMPLEMENTATION_CLASS = "com.github.brodziakm.mavenS3.MavenS3Plugin"
}

object PluginBundle {
  const val VCS = "https://github.com/brodziakm/maven-s3-plugin"
  const val WEBSITE = "https://github.com/brodziakm/maven-s3-plugin"
  const val DISPLAY_NAME = "A plugin to use AWS provider credentials in Maven S3 repositories"
  const val DESCRIPTION = "A plugin to expose a new 'mavenS3' repository type that obtains credentials from the AWS credentials provider chain"
  val TAGS = listOf(
    "plugin",
    "gradle",
    "sample",
    "template"
  )
}