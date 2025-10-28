package io.github.pigaut.orestack.api.event;

import io.github.pigaut.voxel.event.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;

/**
 * Called when a player causes a generator's health to reach 0.
 */
public class GeneratorDestroyEvent extends PlayerEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    public GeneratorDestroyEvent(Player player) {
        super(player);
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

}
