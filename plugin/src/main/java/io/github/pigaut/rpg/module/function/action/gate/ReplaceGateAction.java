package io.github.pigaut.rpg.module.function.action.gate;

import io.github.pigaut.rpg.*;
import io.github.pigaut.rpg.module.gate.*;
import io.github.pigaut.rpg.module.gate.template.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.action.*;
import io.github.pigaut.rpg.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.action.*;
import io.github.pigaut.rpg.module.gate.*;
import io.github.pigaut.rpg.module.gate.template.*;
import org.jetbrains.annotations.*;

public class ReplaceGateAction implements Action {

    private final RpgMakerPlugin plugin;
    private final String gateName;

    public ReplaceGateAction(RpgMakerPlugin plugin, String gateName) {
        this.plugin = plugin;
        this.gateName = gateName;
    }

    @Override
    public void execute(@NotNull Context context) {
        Gate gate = context.get(Gate.class);
        if (gate == null || !gate.exists()) {
            return;
        }

        GateTemplate replacementGate = plugin.getGateTemplate(gateName);
        if (replacementGate != null) {
            gate.replace(replacementGate);
        }
    }
}
