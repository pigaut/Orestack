package io.github.pigaut.orestack.api.event;

import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.jetbrains.annotations.*;

import java.util.*;

/**
 * Called when a player places a generator.
 */
public class GeneratorPlaceEvent extends GeneratorEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Player player;
    private final Set<Block> occupiedBlocks;

    public GeneratorPlaceEvent(Player player, Location origin, String generator, Set<Block> occupiedBlocks) {
        super(origin, generator, 0);
        this.player = player;
        this.occupiedBlocks = occupiedBlocks;
    }

    /**
     * Gets the player who placed the generator.
     *
     * @return the player who placed the generator
     */
    public @NotNull Player getPlayer() {
        return player;
    }

    /**
     * Gets the blocks occupied by the generator.
     *
     * @return a copy of the occupied blocks
     */
    public @NotNull Set<Block> getOccupiedBlocks() {
        return new HashSet<>(occupiedBlocks);
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

}
