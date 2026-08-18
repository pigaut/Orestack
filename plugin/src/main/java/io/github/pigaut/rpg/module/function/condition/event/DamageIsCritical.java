package io.github.pigaut.rpg.module.function.condition.event;

import io.github.pigaut.rpg.event.player.*;
import org.bukkit.event.*;
import org.jetbrains.annotations.*;

public class DamageIsCritical implements EventCondition {

    @Override
    public @Nullable Boolean evaluate(@NotNull Event event) {
        if (!(event instanceof EntityDamageByPlayerEvent playerDamageEvent)) {
            return null;
        }
        return playerDamageEvent.isCritDamage();
    }

}
