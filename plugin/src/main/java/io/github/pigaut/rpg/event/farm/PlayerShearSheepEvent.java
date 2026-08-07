package io.github.pigaut.rpg.event.farm;

import io.github.pigaut.rpg.event.*;
import io.github.pigaut.rpg.event.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.jetbrains.annotations.*;

public class PlayerShearSheepEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Sheep sheep;
    private boolean cancelled;

    public PlayerShearSheepEvent(@NotNull Player player, @NotNull Sheep sheep) {
        super(player);
        this.sheep = sheep;
    }

    public @NotNull Sheep getSheep() {
        return sheep;
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