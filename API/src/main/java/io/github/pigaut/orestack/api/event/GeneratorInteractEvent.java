package io.github.pigaut.orestack.api.event;

import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.block.*;
import org.jetbrains.annotations.*;

/**
 * Called when a player interacts with a non-decorative generator block.
 */
public class GeneratorInteractEvent extends GeneratorEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Player player;
    private final Action action;

    public GeneratorInteractEvent(String generator, int stage, Player player, Action action) {
        super(generator, stage);
        this.player = player;
        this.action = action;
    }

    public @NotNull Player getPlayer() {
        return player;
    }

    public @NotNull Action getAction() {
        return action;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static @NotNull HandlerList getHandlerList() {
        return HANDLERS;
    }

}
