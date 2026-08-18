package io.github.pigaut.rpg.module.function.condition.event;

import org.bukkit.event.*;
import org.bukkit.event.entity.*;
import org.jetbrains.annotations.*;

public class DamageIsFallingCritical implements EventCondition {

    @Override
    public @Nullable Boolean evaluate(@NotNull Event event) {
        if (!(event instanceof EntityDamageByEntityEvent entityDamageEvent)) {
            return null;
        }
        return entityDamageEvent.isCritical();
    }

}
