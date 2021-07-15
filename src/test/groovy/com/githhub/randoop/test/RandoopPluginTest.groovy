package com.githhub.randoop.test

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
    '5.5',
    '6.0',
    '6.4',
    '6.8',
      '7.1'
]

  @Rule TemporaryFolder testProjectDir = new TemporaryFolder()
  def buildFile

  def "setup"() {
    buildFile = testProjectDir.newFile("build.gradle")
  }

  @Unroll def "test applying plugin"() {
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
    GradleRunner.create()
        .withGradleVersion(gradleVersion)
        .withProjectDir(testProjectDir.root)
        .withPluginClasspath()
        .build()

    then:
    noExceptionThrown()

    where:
    gradleVersion << TESTED_GRADLE_VERSIONS
  }
}