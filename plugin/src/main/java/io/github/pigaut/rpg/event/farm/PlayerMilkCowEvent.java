package io.github.pigaut.rpg.event.farm;

import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.player.*;
import org.jetbrains.annotations.*;

public class PlayerMilkCowEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Cow cow;
    private boolean cancelled;

    public PlayerMilkCowEvent(@NotNull Player player, @NotNull Cow cow) {
        super(player);
        this.cow = cow;
    }

    public @NotNull Cow getCow() {
        return cow;
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
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}