package io.github.pigaut.orestack.api.event;

import com.google.common.base.*;
import org.bukkit.block.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

import java.util.*;

/**
 * Called when a player breaks a non-decorative generator block.
 */
public class GeneratorMineEvent extends GeneratorEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Player player;
    private final Block blockMined;
    private boolean idle = false;
    private @Nullable Collection<ItemStack> drops = null;
    private int expToDrop = 0;
    private int toolDamage = 0;

    public GeneratorMineEvent(String generator, int phase, Player player, Block blockMined) {
        super(generator, phase);
        this.player = player;
        this.blockMined = blockMined;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public @NotNull Player getPlayer() {
        return player;
    }

    public @NotNull Block getBlockMined() {
        return blockMined;
    }

    /**
     * Checks whether the generator should keep its current phase after being mined.
     *
     * @return {@code true} if the generator retains its phase; {@code false} if it should go to the previous phase
     */
    public boolean isIdle() {
        return idle;
    }

    /**
     * Sets whether the generator should keep its current phase after being destroyed.
     *
     * @param idle {@code true} to retain the generator's phase; {@code false} to go to the previous phase
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
        Preconditions.checkArgument(expToDrop > -1, "Exp drop must be a positive amount");
        this.expToDrop = expToDrop;
    }

    /**
     * Gets how much damage is applied to the tool when a generator block is mined.
     *
     * @return the damage applied to the tool
     */
    public int getToolDamage() {
        return toolDamage;
    }

    /**
     * Sets how much damage is applied to the tool when a generator block is mined.
     *
     * @param toolDamage the damage to apply to the tool
     * @throws IllegalArgumentException if the value is negative
     */
    public void setToolDamage(int toolDamage) {
        Preconditions.checkArgument(toolDamage > -1, "Tool damage must be a positive amount");
        this.toolDamage = toolDamage;
    }

}
