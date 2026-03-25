package io.github.pigaut.orestack.api.event;

import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.block.*;
import org.jetbrains.annotations.*;

/**
 * Called when a player left-clicks a non-decorative generator block.
 */
public class GeneratorHitEvent extends GeneratorInteractEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    public GeneratorHitEvent(Player player, Action action, Block clickedBlock,
                             Location origin, String generator, int phase) {
        super(player, action, clickedBlock, origin, generator, phase);
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static @NotNull HandlerList getHandlerList() {
        return HANDLERS;
    }

}
