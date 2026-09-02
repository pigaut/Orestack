package io.github.pigaut.rpg.module.function.action.gate;

import io.github.pigaut.rpg.module.gate.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.action.*;
import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.module.function.action.*;
import io.github.pigaut.rpg.module.gate.*;
import io.github.pigaut.yaml.amount.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

public class DamageGateAction implements Action.Executor {

    private final Amount damageAmount;

    public DamageGateAction(Amount damageAmount) {
        this.damageAmount = damageAmount;
    }

    @Override
    public void execute(@NotNull Context context) {
        Player player = context.player();
        if (player == null) {
            return;
        }

        Gate gate = context.get(Gate.class);
        if (gate != null && gate.exists()) {
            gate.damage(player, context, damageAmount.doubleValue());
        }
    }

}
