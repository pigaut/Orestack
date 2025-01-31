package io.github.pigaut.orestack.event;

import io.github.pigaut.orestack.generator.*;
import io.github.pigaut.orestack.player.*;
import io.github.pigaut.orestack.stage.*;
import org.bukkit.block.*;
import org.bukkit.event.*;

public class GeneratorHarvestEvent extends PlayerGeneratorEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    public GeneratorHarvestEvent(OrestackPlayer player, Generator generator) {
        super(player, generator);
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

}
