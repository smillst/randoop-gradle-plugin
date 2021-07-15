package com.githhub.randoop.test

import org.gradle.testkit.runner.GradleRunner
import spock.lang.Unroll

class GenerateTestsTaskTest  extends BasicSpecification {
  private class JavaCode {
    private static def HELLO_WORLD =
        """
      public class HelloWorld {
        public static void main(String[] args) {
          System.out.println("Hello world!");
          args[0].toString();
        }
      }
      """.stripIndent().trim()
  }

  @Unroll def "Project with default source set"() {
    given:
    buildFile << basicBuildFile()

    and:
    def javaSrcDir = testProjectDir.newFolder("src", "main", "java")
    new File(javaSrcDir, "HelloWorld.java") << JavaCode.HELLO_WORLD

    when:
    GradleRunner.create()
        .withProjectDir(testProjectDir.root)
        .withArguments("generateTests")
        .withPluginClasspath()
        .build()

    then: "the error message explains why the code did not compile"
    noExceptionThrown()
  }
}
