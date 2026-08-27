package io.github.pigaut.rpg.module.function.condition.event;

import io.github.pigaut.rpg.event.player.*;
import org.bukkit.event.*;
import org.bukkit.event.entity.*;
import org.jetbrains.annotations.*;

public class DamageIsFallingCritical implements EventCondition {

    @Override
    public @Nullable Boolean evaluate(@NotNull Event event) {
        if (!(event instanceof EntityDamageByPlayerEvent entityDamageEvent)) {
            return null;
        }
        return entityDamageEvent.isFallingCritDamage();
    }

}
