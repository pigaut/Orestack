package io.github.pigaut.rpg.event.item;

import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

public class PlayerGiveItemEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Player player;
    private final ItemStack[] items;
    private boolean cancelled;

    public PlayerGiveItemEvent(@NotNull Player player, @NotNull ItemStack[] items) {
        this.player = player;
        this.items = items;
        this.cancelled = false;
    }

    public @NotNull Player getPlayer() {
        return player;
    }

    public @NotNull ItemStack[] getItems() {
        return items.clone();
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    // Required by Bukkit's event system (called via reflection)
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}

