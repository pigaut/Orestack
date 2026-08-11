package io.github.pigaut.rpg.event.farm;

import io.github.pigaut.rpg.event.player.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.jetbrains.annotations.*;

public class PlayerCollectEggEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Item egg;
    private boolean cancelled;

    public PlayerCollectEggEvent(@NotNull Player player, @NotNull Item egg) {
        super(player);
        this.egg = egg;
    }

    public @NotNull Item getEgg() {
        return egg;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static @NotNull HandlerList getHandlerList() {
        return HANDLERS;
    }
}