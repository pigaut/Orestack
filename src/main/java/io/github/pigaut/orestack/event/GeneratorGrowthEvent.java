package io.github.pigaut.orestack.event;

import io.github.pigaut.orestack.generator.*;
import org.bukkit.event.*;

public class GeneratorGrowthEvent extends GeneratorEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    public GeneratorGrowthEvent(Generator generator) {
        super(generator);
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

}
