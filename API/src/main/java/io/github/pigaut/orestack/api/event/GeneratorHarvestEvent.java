package io.github.pigaut.orestack.api.event;

import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.jetbrains.annotations.*;

/**
 * Called when a player right-clicks a non-decorative generator block.
 */
public class GeneratorHarvestEvent extends GeneratorEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Player player;

    public GeneratorHarvestEvent(String generator, int phase, Player player) {
        super(generator, phase);
        this.player = player;
    }

    public @NotNull Player getPlayer() {
        return player;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static @NotNull HandlerList getHandlerList() {
        return HANDLERS;
    }

}