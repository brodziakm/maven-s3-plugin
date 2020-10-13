# maven-s3-plugin 🐘

A simple Gradle plugin that exposes a 'mavenS3' repository type. This is just a normal 'maven' repository which
delegates it's credential acquisition to the AWS SDK, and allows the optional specification of a profile.
  
## How to use 👣

TODO

## Features 🎨

TODO

## CI ⚙️

This project is using [**GitHub Actions**](https://github.com/brodziakm/maven-s3-plugin/actions).

There are currently the following workflows available:
- [Validate Gradle Wrapper](.github/workflows/gradle-wrapper-validation.yml) - Will check that the gradle wrapper has a valid checksum
- [Pre Merge Checks](.github/workflows/pre-merge.yaml) - Will run the `preMerge` tasks as well as trying to run the Gradle plugin.
- [Publish to Plugin Portal](.github/workflows/pre-merge.yaml) - Will run the `publishPlugin` task when pushing a new tag.

## Acknowledgements

This is based on the Kotlin Gradle plugin template kindly provided by https://github.com/cortinico/kotlin-gradle-plugin-template/generate

## Contributing 🤝

Feel free to open a issue or submit a pull request for any bugs/improvements.
