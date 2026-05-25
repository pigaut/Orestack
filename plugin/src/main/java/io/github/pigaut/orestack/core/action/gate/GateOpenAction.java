package io.github.pigaut.orestack.core.action.gate;

import io.github.pigaut.orestack.gate.*;
import io.github.pigaut.voxel.core.context.*;
import io.github.pigaut.voxel.data.function.action.*;
import org.jetbrains.annotations.*;

public class GateOpenAction implements Action {

    @Override
    public void execute(@NotNull Context context) {
        Gate gate = context.get(Gate.class);
        if (gate != null && gate.exists()) {
            gate.open();
        }
    }

}
