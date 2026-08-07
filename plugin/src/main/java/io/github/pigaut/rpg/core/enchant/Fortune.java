package io.github.pigaut.rpg.core.enchant;

import net.momirealms.craftengine.bukkit.util.*;
import org.bukkit.enchantments.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

import java.util.concurrent.*;

public class Fortune {

    public static int getDropAmount(int fortuneLevel) {
        return getDropAmount(1, fortuneLevel);
    }

    public static int getDropAmount(int baseAmount, int fortuneLevel) {
        if (fortuneLevel <= 0) {
            return baseAmount;
        }

        int bonus = ThreadLocalRandom.current().nextInt(fortuneLevel + 2) - 1;
        if (bonus < 0) {
            bonus = 0;
        }

        return baseAmount * (1 + bonus);
    }

    public static int getDropAmount(@NotNull ItemStack tool) {
        return getDropAmount(getEnchantLevel(tool));
    }

    public static int getDropAmount(@NotNull ItemStack tool, int baseAmount) {
        return getDropAmount(baseAmount, getEnchantLevel(tool));
    }

    public static int getEnchantLevel(@NotNull ItemStack tool) {
        return tool.getEnchantmentLevel(Enchants.FORTUNE);
    }

}
