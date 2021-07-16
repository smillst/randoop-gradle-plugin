package com.githhub.randoop.test

import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import spock.lang.Unroll

class GenerateTestsTaskTest extends BasicSpecification {
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

    private static def HELLO_WORLD_GUAVA =
        """
      import com.google.common.base.Preconditions;
      public class HelloWorld {
        public static void main(String[] args) {
          Preconditions.checkNotNull(args[0]);
          System.out.println("Hello world!");
          args[0].toString();
        }
      }
      """.stripIndent().trim()
  }

  @Unroll
  def "Project with default source set"() {
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

  @Unroll
  def "Project with default source set that has a dependency"() {
    given:
    buildFile <<
        """
       ${basicBuildFile()}
        dependencies {
          implementation 'com.google.guava:guava:30.1.1-jre'
        }
       """.stripIndent()

    and:
    def javaSrcDir = testProjectDir.newFolder("src", "main", "java")
    new File(javaSrcDir, "HelloWorld.java") << JavaCode.HELLO_WORLD_GUAVA

    when:
    GradleRunner.create()
        .withProjectDir(testProjectDir.root)
        .withArguments("generateTests")
        .withPluginClasspath()
        .build()

    then:
    noExceptionThrown()
  }

  @Unroll
  def "Project with custom generateTests task"() {
    given:
    buildFile <<
        """
       ${basicBuildFile()}
        import com.github.randoop.GenerateTests
        task myGenTests(type: GenerateTests) {
          testJar = tasks.jar.getArchiveFile()
        }
       """.stripIndent()

    and:
    def javaSrcDir = testProjectDir.newFolder("src", "main", "java")
    new File(javaSrcDir, "HelloWorld.java") << JavaCode.HELLO_WORLD

    when:
    GradleRunner.create()
        .withProjectDir(testProjectDir.root)
        .withArguments("myGenTests")
        .withPluginClasspath()
        .build()

    then:
    noExceptionThrown()
  }

  @Unroll
  def "Custom generate tasks fails without testJar"() {
    given:
    buildFile <<
        """
       ${basicBuildFile()}
        import com.github.randoop.GenerateTests
        task myGenTests(type: GenerateTests) {
        }
       """.stripIndent()

    and:
    def javaSrcDir = testProjectDir.newFolder("src", "main", "java")
    new File(javaSrcDir, "HelloWorld.java") << JavaCode.HELLO_WORLD

    when:
    BuildResult result = GradleRunner.create()
        .withProjectDir(testProjectDir.root)
        .withArguments("myGenTests")
        .withPluginClasspath()
        .buildAndFail()

    then:
    result.output.contains("Randoop for Java version \"4.2.6")
  }

  @Unroll
  def "Check for different version of Randoop"() {
    given:
    buildFile <<
        """
       ${basicBuildFile()}
        dependencies {
          randoop 'com.github.randoop:randoop:4.2.4'
        }
        import com.github.randoop.GenerateTests
        task myGenTests(type: GenerateTests) {
        }
       """.stripIndent()

    and:
    def javaSrcDir = testProjectDir.newFolder("src", "main", "java")
    new File(javaSrcDir, "HelloWorld.java") << JavaCode.HELLO_WORLD

    when:
    BuildResult result = GradleRunner.create()
        .withProjectDir(testProjectDir.root)
        .withArguments("myGenTests")
        .withPluginClasspath()
        .buildAndFail()

    then:
    result.output.contains("Randoop for Java version \"4.2.4")
  }

//  @Unroll
//  def "Project with additional source sets"() {
//    given:
//    buildFile <<
//        """
//       ${basicBuildFile()}
//        sourceSets {
//          other
//        }
//      """.stripIndent()
//
//    and:
//    def javaSrcDir = testProjectDir.newFolder("src", "main", "java")
//    new File(javaSrcDir, "HelloWorld.java") << JavaCode.HELLO_WORLD
//    def javaSrcOtherDir = testProjectDir.newFolder("src", "other", "java")
//    new File(javaSrcOtherDir, "HelloWorld.java") << JavaCode.HELLO_WORLD
//
//    when:
//    GradleRunner.create()
//        .withProjectDir(testProjectDir.root)
//        .withArguments("generateOtherTests")
//        .withPluginClasspath()
//        .build()
//
//    then:
//    noExceptionThrown()
//  }
}
