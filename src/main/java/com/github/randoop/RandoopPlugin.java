package com.github.randoop;

import java.io.File;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.file.RegularFile;
import org.gradle.api.logging.Logger;
import org.gradle.api.logging.Logging;
import org.gradle.api.provider.Provider;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.SourceSetContainer;
import org.gradle.jvm.tasks.Jar;

public class RandoopPlugin implements Plugin<Project> {
    private final static Logger LOG = Logging.getLogger(RandoopPlugin.class);
    /**
     * Latest version of Randoop to use by default.
     */
    private static final String RANDOOP_VERSION = "4.2.6";

    @Override
    public void apply(Project project) {
        // Add a "randoop" configuration and create a default dependencies to the latest version of randoop.
        Configuration randoop = project.getConfigurations()
                .create("randoop", c -> {
                    c.setVisible(false);
                    c.setCanBeConsumed(false);
                    c.setCanBeResolved(true);
                    c.setDescription("The randoop artifact.");
                    c.defaultDependencies(
                            d -> d.add(project.getDependencies().create("com.github.randoop:randoop:"+RANDOOP_VERSION)));
                });

        project.getTasks().withType(GenerateTests.class).configureEach(
                generateTests -> generateTests.getRandoopJar().from(randoop));

        project.getPluginManager().withPlugin("java", javaPlugin -> {
            LOG.info("Java Plugin found. Adding GenerateTests tasks.");
            // For each source set add a generateTests tasks.
            SourceSetContainer sourceSets = project.getExtensions()
                    .getByType(SourceSetContainer.class);
            sourceSets.forEach(sourceSet -> createGenerateTestTaskForSourceSet(project, sourceSet));
        });
        project.afterEvaluate(x -> {
            if (!project.getPluginManager().hasPlugin("java")) {
                LOG.warn("Java plugin not found, Randoop plugin did not create any tasks.");
            }
        });

    }

    /**
     * If the sourceSet has a jar tasks, then this creates a GenerateTests task for the given source set.
     * @param project project
     * @param sourceSet sourceSet for which to create a GenerateTests tasks
     */
    private void createGenerateTestTaskForSourceSet(Project project, SourceSet sourceSet) {

        String taskName = sourceSet.getTaskName("generate", "tests");

        Jar jarTask = ((Jar) project.getTasks().findByPath(sourceSet.getJarTaskName()));
        if (jarTask == null) {
            // This sourceset does not have a jar task, so don't generate tests for it.
            LOG.info("No jar for " +sourceSet.getName());
            return;
        }
        Provider<RegularFile> testJar = jarTask.getArchiveFile();

        project.getTasks().register(taskName, GenerateTests.class).configure(generateTests -> {
            generateTests.getEmitErrorRevealingTests().set(false);
            generateTests.getTestJar().set(testJar);
            generateTests.getOutputDir()
                    .set(new File(project.getBuildDir(), "randoop/tests/" + sourceSet.getName()));
        });
    }
}

