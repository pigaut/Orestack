package io.github.pigaut.rpg.module.function.action.generator;

import io.github.pigaut.rpg.api.event.generator.*;
import io.github.pigaut.rpg.module.function.action.event.*;
import io.github.pigaut.rpg.api.event.generator.*;
import io.github.pigaut.rpg.module.function.action.event.*;
import org.bukkit.event.*;
import org.jetbrains.annotations.*;

public class GeneratorKeepPhaseAction implements EventAction.Executor {

    @Override
    public void execute(@NotNull Event event) {
        if (event instanceof GeneratorMineEvent mineEvent) {
            mineEvent.setIdle(true);
        }
    }

}
