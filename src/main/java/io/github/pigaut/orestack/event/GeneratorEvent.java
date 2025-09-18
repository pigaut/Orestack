package io.github.pigaut.orestack.event;

import io.github.pigaut.orestack.generator.*;
import io.github.pigaut.voxel.event.*;

public abstract class GeneratorEvent extends CancellableEvent {

    private final Generator generator;

    protected GeneratorEvent(Generator generator) {
        this.generator = generator;
    }

    public Generator getGenerator() {
        return generator;
    }

}
