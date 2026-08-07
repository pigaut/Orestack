package io.github.pigaut.rpg.event.mob;

import io.github.pigaut.rpg.module.mob.*;
import io.github.pigaut.rpg.module.mob.*;
import org.bukkit.event.*;
import org.jetbrains.annotations.*;

public class MobHealthChangeEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Mob mob;

    public MobHealthChangeEvent(@NotNull Mob mob) {
        this.mob = mob;
    }

    public @NotNull Mob getMob() {
        return mob;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static @NotNull HandlerList getHandlerList() {
        return HANDLERS;
    }

}
