package io.github.pigaut.rpg.module.function.condition.player.tool;

import io.github.pigaut.yaml.amount.*;
import org.bukkit.enchantments.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;
import org.jetbrains.annotations.*;

public class PlayerToolHasEnchant implements ToolCondition {

    private final Enchantment enchantment;
    private final Amount level;

    public PlayerToolHasEnchant(@NotNull Enchantment enchantment, @NotNull Amount level) {
        this.enchantment = enchantment;
        this.level = level;
    }

    @Override
    public @Nullable Boolean evaluate(@NotNull ItemStack tool) {
        if (tool.hasItemMeta()) {
            final ItemMeta meta = tool.getItemMeta();
            return meta.hasEnchant(enchantment) && level.match(meta.getEnchantLevel(enchantment));
        }
        return false;
    }
}
