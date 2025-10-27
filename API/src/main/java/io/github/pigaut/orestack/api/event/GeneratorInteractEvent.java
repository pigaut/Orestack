package io.github.pigaut.orestack.api.event;

import io.github.pigaut.voxel.event.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.block.*;
import org.jetbrains.annotations.*;

/**
 * Called when a player interacts with a non-decorative generator block.
 */
public class GeneratorInteractEvent extends PlayerEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Action action;

    public GeneratorInteractEvent(@NotNull Player player, @NotNull Action action) {
        super(player);
        this.action = action;
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
