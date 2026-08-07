package io.github.pigaut.rpg.event.item;

import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

public class PlayerTakeItemEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Player player;
    private final ItemStack item;
    private final int amount;
    private boolean cancelled;

    public PlayerTakeItemEvent(@NotNull Player player, @NotNull ItemStack item, int amount) {
        this.player = player;
        this.item = item;
        this.amount = amount;
        this.cancelled = false;
    }

    public @NotNull Player getPlayer() {
        return player;
    }

    public @NotNull ItemStack getItem() {
        return item.clone();
    }

    public int getAmount() {
        return amount;
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

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
