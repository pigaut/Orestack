package io.github.pigaut.rpg.event.mob;

import io.github.pigaut.rpg.module.mob.Mob;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.jetbrains.annotations.*;

public class MobDeathEvent extends MobEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    public MobDeathEvent(@NotNull Mob mob, @NotNull Entity enemy) {
        super(mob, enemy);
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static @NotNull HandlerList getHandlerList() {
        return HANDLERS;
    }

}
