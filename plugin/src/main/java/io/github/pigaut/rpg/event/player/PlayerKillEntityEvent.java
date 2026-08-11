package io.github.pigaut.rpg.event.player;

import org.bukkit.entity.*;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PlayerKillEntityEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Player player;
    private final LivingEntity entity;
    private boolean cancelled = false;

    public PlayerKillEntityEvent(@NotNull Player player, @NotNull LivingEntity entity) {
        this.player = player;
        this.entity = entity;
    }

    public @NotNull Player getPlayer() {
        return player;
    }

    public @NotNull LivingEntity getEntity() {
        return entity;
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

    public static @NotNull HandlerList getHandlerList() {
        return HANDLERS;
    }

}