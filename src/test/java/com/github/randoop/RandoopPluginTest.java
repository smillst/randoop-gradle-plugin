package com.github.randoop;

import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.GradleRunner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.gradle.testkit.runner.TaskOutcome.SUCCESS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RandoopPluginTest {

    @TempDir
    File testProjectDir;
    private File buildFile;

    @BeforeEach
    public void setup() {
        buildFile = new File(testProjectDir, "build.gradle");
    }

    @Test
    public void testHelloWorldTask() throws IOException {
        String buildFileContent = "plugins { id 'com.github.randoop' }\n"
                + "randoop{hello = 'Hello'}";

        writeFile(buildFile, buildFileContent);

        BuildResult result = GradleRunner.create()
                .withProjectDir(testProjectDir)
                .withPluginClasspath()
                .withArguments("runRandoop")
//                .withGradleVersion("6.0.1")
                .build();

        assertTrue(result.getOutput().contains("Hello world!"));
        assertEquals(SUCCESS, result.task(":runRandoop").getOutcome());
    }

    @Test
    public void testHelloWorldTask2() throws IOException {
        String buildFileContent = "plugins { id 'com.github.randoop' }\n"
                + "randoop{hello = 'Goodbye'}";

        writeFile(buildFile, buildFileContent);

        BuildResult result = GradleRunner.create()
                .withProjectDir(testProjectDir)
                .withPluginClasspath()
                .withArguments("runRandoop")
//                .withGradleVersion("6.0.1")
                .build();

        assertTrue(result.getOutput().contains("Goodbye world!"));
        assertEquals(SUCCESS, result.task(":runRandoop").getOutcome());
    }

    @Test
    public void testHelloWorldTask3() throws IOException {
        String buildFileContent = "plugins { id 'com.github.randoop' }";
        writeFile(buildFile, buildFileContent);

        BuildResult result = GradleRunner.create()
                .withProjectDir(testProjectDir)
                .withPluginClasspath()
                .withArguments("runRandoop")
//                .withGradleVersion("6.0.1")
                .build();

        assertTrue(result.getOutput().contains("Hello world!"));
        assertEquals(SUCCESS, result.task(":runRandoop").getOutcome());
    }

    private void writeFile(File destination, String content) throws IOException {
        BufferedWriter output = null;
        try {
            output = new BufferedWriter(new FileWriter(destination));
            output.write(content);
        } finally {
            if (output != null) {
                output.close();
            }
        }
    }
}
