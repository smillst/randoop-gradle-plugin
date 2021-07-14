package com.github.randoop;

import org.gradle.api.provider.Property;

public abstract class RandoopExtension {
    abstract public Property<String> getHello();
}
