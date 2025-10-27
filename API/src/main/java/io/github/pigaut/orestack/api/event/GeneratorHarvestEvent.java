package io.github.pigaut.orestack.api.event;

import io.github.pigaut.voxel.event.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.jetbrains.annotations.*;

/**
 * Called when a player right-clicks a non-decorative generator block.
 */
public class GeneratorHarvestEvent extends PlayerEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    public GeneratorHarvestEvent(@NotNull Player player) {
        super(player);
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static @NotNull HandlerList getHandlerList() {
        return HANDLERS;
    }

}