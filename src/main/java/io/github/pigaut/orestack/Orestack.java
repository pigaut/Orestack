package io.github.pigaut.orestack;

import io.github.pigaut.orestack.options.*;
import io.github.pigaut.yaml.amount.*;
import org.bukkit.block.*;
import org.bukkit.enchantments.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

public class Orestack {

    private static final OrestackPlugin plugin = OrestackPlugin.getPlugin();

    public static double getGeneratorDamage(@NotNull Player player, @NotNull Block block) {
        OrestackOptionsManager options = plugin.getOrestackOptions();

        ItemStack tool = player.getInventory().getItemInMainHand();
        Amount baseDamage = options.getToolDamage(tool.getType(), block.getType());

        if (options.isEfficiencyDamage()) {
            int efficiencyLevel = tool.getEnchantmentLevel(Enchantment.EFFICIENCY);
            if (efficiencyLevel != 0) {
                baseDamage = baseDamage.transform(value -> value + efficiencyLevel);
            }
        }

        double damage = baseDamage.getDouble();
        if (options.isReducedCooldownDamage()) {
            damage *= player.getAttackCooldown();
        }

        return damage;
    }

}
