object Versions {
  const val JUNIT = "4.13"
  const val KTLINT = "0.39.0"
  const val AWSSDK = "2.15.7"
}

object BuildPluginsVersion {
  const val DETEKT = "1.10.0"
  const val KOTLIN = "1.4.10"
  const val KTLINT = "9.4.1"
  const val PLUGIN_PUBLISH = "0.12.0"
  const val VERSIONS_PLUGIN = "0.33.0"
}

object AwsSdk {
  const val AUTH = "software.amazon.awssdk:auth:${Versions.AWSSDK}"
  const val STS = "software.amazon.awssdk:sts:${Versions.AWSSDK}"
}

object TestingLib {
  const val JUNIT = "junit:junit:${Versions.JUNIT}"
}