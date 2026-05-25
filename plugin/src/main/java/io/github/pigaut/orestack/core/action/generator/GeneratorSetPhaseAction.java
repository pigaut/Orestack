package io.github.pigaut.orestack.core.action.generator;

import io.github.pigaut.orestack.generator.*;
import org.jetbrains.annotations.*;

public class GeneratorSetPhaseAction implements GeneratorAction {

    private final int phase;
    private final boolean growing;

    public GeneratorSetPhaseAction(int phase, boolean growing) {
        this.phase = phase;
        this.growing = growing;
    }

    @Override
    public void execute(@NotNull Generator generator) {
        generator.setPhase(phase, growing);
    }

}
