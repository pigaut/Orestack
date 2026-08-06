package io.github.pigaut.rpg.api.event.generator;

import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.jetbrains.annotations.*;

/**
 * Called when a player causes a generator's health to reach 0.
 */
public class GeneratorDestroyEvent extends GeneratorEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Player player;

    public GeneratorDestroyEvent(Player player, Location origin, String generator, int phase) {
        super(origin, generator, phase);
        this.player = player;
    }

    public @NotNull Player getPlayer() {
        return player;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

}
