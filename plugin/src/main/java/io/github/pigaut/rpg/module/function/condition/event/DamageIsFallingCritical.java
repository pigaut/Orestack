package io.github.pigaut.rpg.module.function.condition.event;

import io.github.pigaut.rpg.core.context.*;
import io.github.pigaut.rpg.event.player.*;
import io.github.pigaut.rpg.module.function.response.*;
import org.bukkit.event.*;
import org.bukkit.event.entity.*;
import org.jetbrains.annotations.*;

public class DamageIsFallingCritical implements EventCondition {

    @Override
    public @NotNull FunctionResponse evaluate(@NotNull Event event) {
        if (!(event instanceof EntityDamageByPlayerEvent entityDamageEvent)) {
            return new FunctionError("Event that triggered the function is not an EntityDamageByPlayerEvent");
        }
        return FunctionResponse.met(entityDamageEvent.isFallingCritDamage());
    }

}
