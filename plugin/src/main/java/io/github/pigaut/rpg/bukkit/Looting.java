package io.github.pigaut.rpg.bukkit;

import io.github.pigaut.rpg.core.enchant.*;
import io.github.pigaut.rpg.core.enchant.*;
import io.lumine.mythic.bukkit.utils.items.*;
import org.bukkit.enchantments.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

import java.util.concurrent.*;

public class Looting {

    public static int getDropAmount(int lootingLevel) {
        return getDropAmount(1, lootingLevel);
    }

    public static int getDropAmount(int baseAmount, int lootingLevel) {
        if (lootingLevel <= 0) {
            return baseAmount;
        }

        return baseAmount + ThreadLocalRandom.current().nextInt(lootingLevel + 1);
    }

    public static int getDropAmount(@NotNull ItemStack weapon) {
        return getDropAmount(getEnchantLevel(weapon));
    }

    public static int getDropAmount(@NotNull ItemStack weapon, int baseAmount) {
        return getDropAmount(baseAmount, getEnchantLevel(weapon));
    }

    public static int getEnchantLevel(@NotNull ItemStack weapon) {
        return weapon.getEnchantmentLevel(Enchants.LOOTING);
    }

}
