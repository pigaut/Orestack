package io.github.pigaut.orestack.core.action;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.generator.*;
import io.github.pigaut.orestack.generator.global.*;
import io.github.pigaut.voxel.data.function.action.block.*;
import org.bukkit.block.*;
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
