package io.github.pigaut.rpg.event.mob;

import io.github.pigaut.rpg.event.*;
import io.github.pigaut.rpg.module.mob.Mob;
import io.github.pigaut.rpg.event.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

public abstract class MobEvent extends CancellableEvent {

    private final Mob mob;
    private final Entity enemy;

    public MobEvent(@NotNull Mob mob, @NotNull Entity enemy) {
        this.mob = mob;
        this.enemy = enemy;
    }

    public @NotNull Mob getMob() {
        return mob;
    }

    public @NotNull Entity getEnemy() {
        return enemy;
    }

}
