# Randoop Gradle Plugin
A Gradle Plugin for Randoop https://randoop.github.io/randoop/

# Installing the plugin
1. Install Randoop to Maven local. You can do this with an existing Jar:
  ```
  mvn install:install-file -Dfile=<path-to-file> -DgroupId="com.github.randoop" -DartifactId=randoop -Dversion="4.2.6" -Dpackaging="jar"
  ```
  Or in the main directory of randoop, run:
  ```
  ./gradlew publishLocalPublicationToMavenRepository
  ```
2. Next in this repository, run 
  ```
  ./gradlew publishToMavenLocal
  ```
3. Then add the following to your build script:
  ```
  plugins {
    id 'com.github.randoop' version '0.0.1-SNAPSHOT'
  }

  repositories {
    maven {
      mavenLocal()
    }
  }
  ```

# Usage

By default, this plugin creates a `GenerateTests` task for each source set
that has a corresponding `Jar` task.  For the main source set, this task
is called `generateTests`. 
