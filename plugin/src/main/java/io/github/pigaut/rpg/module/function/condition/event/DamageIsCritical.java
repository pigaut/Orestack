package io.github.pigaut.rpg.module.function.condition.event;

import io.github.pigaut.rpg.event.player.*;
import io.github.pigaut.rpg.module.function.response.*;
import org.bukkit.event.*;
import org.jetbrains.annotations.*;

public class DamageIsCritical implements EventCondition {

    @Override
    public @NotNull FunctionResponse evaluate(@NotNull Event event) {
        if (!(event instanceof EntityDamageByPlayerEvent playerDamageEvent)) {
            return new FunctionError("Event that triggered function is not an EntityDamagedByPlayer event");
        }
        return FunctionResponse.met(playerDamageEvent.isCritDamage());
    }

}
