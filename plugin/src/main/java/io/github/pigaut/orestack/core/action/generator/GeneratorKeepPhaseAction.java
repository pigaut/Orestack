package io.github.pigaut.orestack.core.action.generator;

import io.github.pigaut.orestack.api.event.*;
import io.github.pigaut.voxel.data.function.action.event.*;
import org.bukkit.event.*;
import org.jetbrains.annotations.*;

public class GeneratorKeepPhaseAction implements EventAction {

    @Override
    public void execute(@NotNull Event event) {
        if (event instanceof GeneratorMineEvent mineEvent) {
            mineEvent.setIdle(true);
        }
    }

}
