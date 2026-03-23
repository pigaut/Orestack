package io.github.pigaut.orestack.api.event;

import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.jetbrains.annotations.*;

/**
 * Called when a player left-clicks a non-decorative generator block.
 */
public class GeneratorHitEvent extends GeneratorEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Player player;

    public GeneratorHitEvent(String generator, int phase, Player player) {
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
