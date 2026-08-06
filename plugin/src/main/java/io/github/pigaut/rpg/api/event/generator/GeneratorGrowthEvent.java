package io.github.pigaut.rpg.api.event.generator;

import org.bukkit.*;
import org.bukkit.event.*;

/**
 * Called when a generator grows to the next phase.
 */
public class GeneratorGrowthEvent extends GeneratorEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    public GeneratorGrowthEvent(Location origin, String generator, int phase) {
        super(origin, generator, phase);
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

}
