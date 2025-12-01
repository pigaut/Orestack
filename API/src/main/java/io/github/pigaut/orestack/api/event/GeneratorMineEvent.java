package io.github.pigaut.orestack.api.event;

import io.github.pigaut.voxel.event.*;
import io.github.pigaut.yaml.util.*;
import org.bukkit.block.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

import java.util.*;

/**
 * Called when a player breaks a non-decorative generator block.
 */
public class GeneratorMineEvent extends PlayerEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Block blockMined;
    private boolean idle = false;
    private @Nullable Collection<ItemStack> drops = null;
    private int expToDrop = 0;

    public GeneratorMineEvent(@NotNull Player player, @NotNull Block blockMined) {
        super(player);
        this.blockMined = blockMined;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public @NotNull Block getBlockMined() {
        return blockMined;
    }

    /**
     * Checks whether the generator should keep its current stage after being mined.
     *
     * @return {@code true} if the generator retains its stage; {@code false} if it should go to the previous stage
     */
    public boolean isIdle() {
        return idle;
    }

    /**
     * Sets whether the generator should keep its current stage after being destroyed.
     *
     * @param idle {@code true} to retain the generator's stage; {@code false} to go to the previous stage
     */
    public void setIdle(boolean idle) {
        this.idle = idle;
    }

    /**
     * Gets the item drops for this event.
     *
     * @return a collection of ItemStacks to drop, or {@code null} if no items should be dropped
     */
    public @Nullable Collection<ItemStack> getItemDrops() {
        return drops;
    }

    /**
     * Sets the item drops for this event.
     *
     * @param drops the items to drop, or {@code null} to drop nothing
     */
    public void setItemDrops(@Nullable Collection<ItemStack> drops) {
        this.drops = drops;
    }

    /**
     * Gets the amount of experience to drop.
     *
     * @return the experience amount (always non-negative)
     */
    public int getExpDrops() {
        return expToDrop;
    }

    /**
     * Sets the amount of experience to drop.
     *
     * @param expToDrop the experience amount (must be zero or positive)
     * @throws IllegalArgumentException if the value is negative
     */
    public void setExpDrops(int expToDrop) {
        Preconditions.checkArgument(expToDrop > -1, "Exp drop amount must be a positive number.");
        this.expToDrop = expToDrop;
    }

}
