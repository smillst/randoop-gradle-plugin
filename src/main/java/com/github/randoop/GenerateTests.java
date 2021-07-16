package com.github.randoop;

import org.gradle.api.file.ConfigurableFileCollection;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.InputFile;
import org.gradle.api.tasks.InputFiles;
import org.gradle.api.tasks.JavaExec;
import org.gradle.api.tasks.Optional;
import org.gradle.api.tasks.OutputDirectory;

/**
 * A task class that runs Randoop with the "gentests" option.
 */
public abstract class GenerateTests extends JavaExec {
    @InputFiles
    abstract ConfigurableFileCollection getRandoopJar();

    /**
     *
     * @return
     */
    @Input
    @Optional
    abstract Property<Boolean> getEmitErrorRevealingTests();

    /**
     * Treat every class in the given jar file as a class to test. Passed to Randoop via {@code --testjar} option.
     * @return the jar to generate tests for
     */
    @InputFile
    @Optional
    abstract RegularFileProperty getTestJar();

    /**
     * Directory to write tests.
     * @return the directory where the tests are written
     */
    @OutputDirectory
    // TODO: This should output in a different directory every time.
    @Optional // TODO: This should not be optional.
    abstract DirectoryProperty getOutputDir();

    public GenerateTests() {
        getMainClass().set("randoop.main.Main");
    }
    @Override
    public void exec(){
        classpath(getRandoopJar());

        args("gentests");
        if(getTestJar().isPresent()) {
            // The Jar file to use must be explicitly on the classpath.
            classpath(getTestJar().get());
            args("--testjar");
            args(getTestJar().get());
        }
        args("--time-limit=15");
        args("--progressintervalsteps=-1");
        if(getOutputDir().isPresent()) {
            args("--junit-output-dir");
            args(getOutputDir().get().getAsFile().getAbsolutePath());
        }
        if (!getEmitErrorRevealingTests().getOrElse(true)) {
            args("--no-error-revealing-tests=true");
        }

        super.exec();
    }
}