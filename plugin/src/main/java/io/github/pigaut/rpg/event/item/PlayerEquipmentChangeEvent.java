package io.github.pigaut.rpg.event.item;

import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

public class PlayerEquipmentChangeEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Player player;
    private final EquipmentSlot slot;
    private final ItemStack oldItem;
    private final ItemStack newItem;

    public PlayerEquipmentChangeEvent(Player player, EquipmentSlot slot, ItemStack oldItem, ItemStack newItem) {
        this.player = player;
        this.slot = slot;
        this.oldItem = oldItem;
        this.newItem = newItem;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public @NotNull Player getPlayer() {
        return player;
    }

    public @NotNull EquipmentSlot getSlot() {
        return slot;
    }

    public @NotNull ItemStack getOldItem() {
        return oldItem;
    }

    public @NotNull ItemStack getNewItem() {
        return newItem;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
}
