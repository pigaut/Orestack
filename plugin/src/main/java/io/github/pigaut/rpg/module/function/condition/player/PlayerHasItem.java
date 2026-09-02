package io.github.pigaut.rpg.module.function.condition.player;

import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

public class PlayerHasItem implements PlayerCondition.Predicate {

    private final ItemStack item;

    public PlayerHasItem(ItemStack item) {
        this.item = item;
    }

    @Override
    public boolean test(@NotNull Player player) {
        return player.getInventory().containsAtLeast(item, item.getAmount());
    }

}
