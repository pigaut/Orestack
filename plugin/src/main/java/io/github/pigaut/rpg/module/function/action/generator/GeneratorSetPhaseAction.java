package io.github.pigaut.rpg.module.function.action.generator;

import io.github.pigaut.rpg.module.generator.*;
import io.github.pigaut.rpg.module.generator.*;
import org.jetbrains.annotations.*;

public class GeneratorSetPhaseAction implements GeneratorAction.Executor {

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
