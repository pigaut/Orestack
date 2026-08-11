package io.github.pigaut.rpg.event.player;

import org.bukkit.block.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

public class PlayerCollectBrewedPotionEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();
    private boolean cancelled;

    private final Player player;
    private final Block brewingStand;
    private final ItemStack potion;
    private final int amount;

    public PlayerCollectBrewedPotionEvent(@NotNull Player player, @NotNull Block brewingStand,
                                          @NotNull ItemStack potion, int amount) {
        this.player = player;
        this.brewingStand = brewingStand;
        this.potion = potion;
        this.amount = amount;
    }

    public @NotNull Player getPlayer() {
        return player;
    }

    public @NotNull Block getBrewingStand() {
        return brewingStand;
    }

    public @NotNull ItemStack getPotion() {
        return potion;
    }

    public int getAmount() {
        return amount;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static @NotNull HandlerList getHandlerList() {
        return HANDLERS;
    }
}