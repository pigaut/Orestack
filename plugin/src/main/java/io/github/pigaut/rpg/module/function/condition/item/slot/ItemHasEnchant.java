package io.github.pigaut.rpg.module.function.condition.item.slot;

import io.github.pigaut.yaml.amount.*;
import org.bukkit.enchantments.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;
import org.jetbrains.annotations.*;

public class ItemHasEnchant implements ItemPredicate {

    private final Enchantment enchantment;
    private final Amount level;

    public ItemHasEnchant(@NotNull Enchantment enchantment, @NotNull Amount level) {
        this.enchantment = enchantment;
        this.level = level;
    }

    @Override
    public boolean test(@NotNull ItemStack item) {
        if (!item.hasItemMeta()) {
            return false;
        }
        ItemMeta meta = item.getItemMeta();
        return meta.hasEnchant(enchantment) && level.match(meta.getEnchantLevel(enchantment));
    }
}
