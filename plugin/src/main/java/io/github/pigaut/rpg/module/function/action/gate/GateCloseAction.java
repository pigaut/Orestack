package io.github.pigaut.rpg.module.function.action.gate;

import io.github.pigaut.rpg.module.gate.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.action.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.action.*;
import io.github.pigaut.rpg.module.gate.*;
import org.jetbrains.annotations.*;

public class GateCloseAction implements Action {

    @Override
    public void execute(@NotNull Context context) {
        Gate gate = context.get(Gate.class);
        if (gate != null && gate.exists()) {
            gate.close();
        }
    }

}
