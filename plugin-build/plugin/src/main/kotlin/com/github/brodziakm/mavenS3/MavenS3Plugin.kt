package com.github.brodziakm.mavenS3

import org.gradle.api.Plugin
import org.gradle.api.Project

abstract class MavenS3Plugin : Plugin<Project> {
  override fun apply(project: Project) {
    // Add the 'template' extension object
    val extension = project.extensions.create("templateExampleConfig", TemplateExtension::class.java, project)

    // Add a task that uses configuration from the extension object
    project.tasks.register("templateExample", TemplateExampleTask::class.java) {
      it.tag.set(extension.tag)
      it.message.set(extension.message)
      it.outputFile.set(extension.outputFile)
    }
  }
}
