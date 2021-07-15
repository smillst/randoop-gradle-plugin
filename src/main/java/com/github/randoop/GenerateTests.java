package com.github.randoop;

import org.gradle.api.file.ConfigurableFileCollection;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.file.FileCollection;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.InputFile;
import org.gradle.api.tasks.InputFiles;
import org.gradle.api.tasks.JavaExec;
import org.gradle.api.tasks.OutputDirectory;

/**
 * A task class that runs Randoop with the "gentests" option.
 */
public abstract class GenerateTests extends JavaExec {
    @InputFiles
    abstract ConfigurableFileCollection getRandoopClassPath();

    /**
     *
     * @return
     */
    @Input
    abstract Property<Boolean> getEmitErrorRevealingTests();

    /**
     * Treat every class in the given jar file as a class to test. Passed to Randoop via {@code --testjar} option.
     * @return the jar to generate tests for
     */
    @InputFile
    abstract RegularFileProperty getTestJar();

    /**
     * Directory to write tests.
     * @return the directory where the tests are written
     */
    @OutputDirectory
    // TODO: This should output in a different directory every time.
    abstract DirectoryProperty getOutputDir();

    public GenerateTests() {
        getMainClass().set("randoop.main.Main");
        getEmitErrorRevealingTests().convention(true);
    }
    @Override
    public void exec(){
        setClasspath(getRandoopClassPath());
        // The Jar file to use must be explicitly on the classpath.
        classpath(getTestJar().get());
        args("gentests");
        args("--testjar");
        args(getTestJar().get());
        args("--time-limit=15");
        args("--progressintervalsteps=-1");
        args("--junit-output-dir");
        args(getOutputDir().get().getAsFile().getAbsolutePath());
        if (!getEmitErrorRevealingTests().get()) {
            args("--no-error-revealing-tests=true");
        }

        super.exec();
    }
}