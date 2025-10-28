package io.github.pigaut.orestack.api.event;

import io.github.pigaut.voxel.event.*;
import org.bukkit.block.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.jetbrains.annotations.*;

/**
 * Called when a player breaks a non-decorative generator block.
 */
public class GeneratorMineEvent extends PlayerEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Block blockMined;
    private boolean keepStage;

    public GeneratorMineEvent(@NotNull Player player, @NotNull Block blockMined, boolean keepStage) {
        super(player);
        this.blockMined = blockMined;
        this.keepStage = keepStage;
    }

    public @NotNull Block getBlockMined() {
        return blockMined;
    }

    /**
     * Checks whether the generator should keep its current stage after being mined.
     *
     * @return {@code true} if the generator retains its stage; {@code false} if it should go to the previous stage
     */
    public boolean isKeepStage() {
        return keepStage;
    }

    /**
     * Sets whether the generator should keep its current stage after being destroyed.
     *
     * @param keepStage {@code true} to retain the generator's stage; {@code false} to go to the previous stage
     */
    public void setKeepStage(boolean keepStage) {
        this.keepStage = keepStage;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

}
