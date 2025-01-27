package io.github.pigaut.orestack.event;

import io.github.pigaut.orestack.generator.*;
import io.github.pigaut.orestack.player.*;
import io.github.pigaut.orestack.stage.*;
import org.bukkit.block.*;
import org.bukkit.event.*;
import org.jetbrains.annotations.*;

public class GeneratorInteractEvent extends PlayerGeneratorEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    public GeneratorInteractEvent(OrestackPlayer player, Block block, Generator generator, GeneratorStage stage) {
        super(player, block, generator, stage);
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static @NotNull HandlerList getHandlerList() {
        return HANDLERS;
    }

}
