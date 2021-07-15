package com.githhub.randoop.test

import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import spock.lang.Unroll;



class RandoopPluginTest extends BasicSpecification {

  @Unroll def "test applying plugin without Java plugin"() {
    given:
    buildFile <<
        """
        plugins {
          id 'com.github.randoop'
        }

        repositories {
          maven {
            mavenLocal()
          }
        }
      """.stripIndent().trim()

    when:
    BuildResult result = GradleRunner.create()
        .withGradleVersion(gradleVersion)
        .withProjectDir(testProjectDir.root)
        .withPluginClasspath()
        .withArguments("-info")
        .build()

    then:
    result.output.contains('Java plugin not found, Randoop plugin did not create any tasks')

    where:
    gradleVersion << TESTED_GRADLE_VERSIONS
  }

  @Unroll def "test applying plugin with Java plugin"() {
    given:
    buildFile << basicBuildFile()

    when:
    BuildResult result = GradleRunner.create()
        .withGradleVersion(gradleVersion)
        .withProjectDir(testProjectDir.root)
        .withPluginClasspath()
        .withArguments("-info")
        .build()

    then:
    result.output.contains('Java Plugin found. Adding GenerateTests tasks.')

    where:
    gradleVersion << TESTED_GRADLE_VERSIONS
  }

  @Unroll def "test applying plugin with Java plugin after Randoop"() {
    given:
    buildFile <<
        """
        plugins {
          id 'com.github.randoop'
          id 'java'
        }

        repositories {
          maven {
            mavenLocal()
          }
        }
      """.stripIndent().trim()

    when:
    BuildResult result = GradleRunner.create()
        .withGradleVersion(gradleVersion)
        .withProjectDir(testProjectDir.root)
        .withPluginClasspath()
        .withArguments("-info")
        .build()

    then:
    result.output.contains('Java Plugin found. Adding GenerateTests tasks.')

    where:
    gradleVersion << TESTED_GRADLE_VERSIONS
  }
}
