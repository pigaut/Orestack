package io.github.pigaut.rpg.module.item.rarity;

import org.jetbrains.annotations.*;

public class ItemRarity {

    private final String name;
    private final String display;
    private final String itemName;

    public ItemRarity(@NotNull String name, @NotNull String display, @NotNull String itemName) {
        this.name = name;
        this.display = display;
        this.itemName = itemName;
    }

    public @NotNull String getName() {
        return name;
    }

    public @NotNull String getDisplay() {
        return display;
    }

    public @NotNull String getItemName() {
        return itemName;
    }

}