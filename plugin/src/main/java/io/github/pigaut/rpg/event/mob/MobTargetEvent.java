package io.github.pigaut.rpg.event.mob;

import io.github.pigaut.rpg.event.*;
import io.github.pigaut.rpg.module.mob.Mob;
import io.github.pigaut.rpg.event.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.jetbrains.annotations.*;

public class MobTargetEvent extends CancellableEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Mob mob;
    private final LivingEntity target;

    public MobTargetEvent(@NotNull Mob mob, @Nullable LivingEntity target) {
        this.mob = mob;
        this.target = target;
    }

    public @NotNull Mob getMob() {
        return mob;
    }

    public @Nullable LivingEntity getTarget() {
        return target;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static @NotNull HandlerList getHandlerList() {
        return HANDLERS;
    }

}
