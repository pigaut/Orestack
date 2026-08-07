package io.github.pigaut.rpg.event.item;

import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

public class ItemEquipEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Player player;
    private final ItemStack item;

    public ItemEquipEvent(Player player, ItemStack item) {
        this.player = player;
        this.item = item;
    }

    public @NotNull Player getPlayer() {
        return player;
    }

    public @NotNull ItemStack getItem() {
        return item;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static @NotNull HandlerList getHandlerList() {
        return HANDLERS;
    }

}
