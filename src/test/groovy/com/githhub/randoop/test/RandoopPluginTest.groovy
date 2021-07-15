package com.githhub.randoop.test

import org.gradle.launcher.daemon.protocol.Build
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification
import spock.lang.Unroll;



class RandoopPluginTest extends Specification {

  static final List<String> TESTED_GRADLE_VERSIONS = [
//    '3.4',
//    '4.0',
//    '4.8',
//    '4.9',
//    '4.10',
//    '5.0',
//    '5.5',
//    '6.0',
//    '6.4',
//    '6.8',
      '7.1'
]

  @Rule TemporaryFolder testProjectDir = new TemporaryFolder()
  def buildFile

  def "setup"() {
    buildFile = testProjectDir.newFile("build.gradle")
  }

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
    buildFile <<
        """
        plugins {
          id 'java'
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
