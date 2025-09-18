package io.github.pigaut.orestack.event;

import io.github.pigaut.orestack.generator.*;
import io.github.pigaut.orestack.player.*;
import org.bukkit.event.*;
import org.jetbrains.annotations.*;

public class GeneratorInteractEvent extends PlayerGeneratorEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    public GeneratorInteractEvent(OrestackPlayer player, Generator generator) {
        super(player, generator);
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static @NotNull HandlerList getHandlerList() {
        return HANDLERS;
    }

}
