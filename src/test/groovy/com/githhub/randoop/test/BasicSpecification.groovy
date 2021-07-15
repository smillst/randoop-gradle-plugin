package com.githhub.randoop.test

import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

class BasicSpecification extends Specification{

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

  /**
   * Returns a build file that applies randoop and java plugins.
   * @return a build file that applies randoop and java plugins
   */
   static def basicBuildFile() {
    """
    plugins {
      id "java"
      id "com.github.randoop"
    }

    repositories {
        mavenLocal()
        mavenCentral()
    }
    """.stripIndent()
  }
}
