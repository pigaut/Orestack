package io.github.pigaut.rpg.event;

import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

public class PlaceItemInInventoryEvent extends CancellableEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Inventory inventory;
    private final Player player;
    private final ItemStack item;
    private final int slot;
    private final boolean replaced;

    public PlaceItemInInventoryEvent(@NotNull Inventory inventory, @NotNull Player player,
                                     @NotNull ItemStack item, int slot, boolean replaced) {
        this.inventory = inventory;
        this.player = player;
        this.item = item;
        this.slot = slot;
        this.replaced = replaced;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public Player getPlayer() {
        return player;
    }

    public ItemStack getItem() {
        return item;
    }

    public int getSlot() {
        return slot;
    }

    public boolean isReplaced() {
        return replaced;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static @NotNull HandlerList getHandlerList() {
        return HANDLERS;
    }

}
