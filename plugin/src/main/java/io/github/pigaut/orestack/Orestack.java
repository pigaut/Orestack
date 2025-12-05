package io.github.pigaut.orestack;

import io.github.pigaut.orestack.generator.template.*;
import io.github.pigaut.orestack.settings.*;
import io.github.pigaut.yaml.amount.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.enchantments.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

public class Orestack {

    private static final OrestackPlugin plugin = OrestackPlugin.getInstance();

    public static GeneratorTemplate getGeneratorTemplate(String name) {
        return plugin.getGeneratorTemplate(name);
    }

    public static boolean isGenerator(Location location) {
        return plugin.getGenerators().isGenerator(location);
    }

    public static double getGeneratorDamage(@NotNull Player player, @NotNull Block block) {
        OrestackSettings settings = plugin.getSettings();

        ItemStack tool = player.getInventory().getItemInMainHand();
        Amount baseDamage = settings.getToolDamage(tool.getType(), block.getType());

        if (settings.isEfficiencyDamage()) {
            int efficiencyLevel = tool.getEnchantmentLevel(Enchantment.EFFICIENCY);
            if (efficiencyLevel != 0) {
                baseDamage = baseDamage.transform(value -> value + efficiencyLevel);
            }
        }

        double damage = baseDamage.getDouble();
        if (settings.isReducedCooldownDamage()) {
            damage *= player.getAttackCooldown();
        }

        return damage;
    }

}
