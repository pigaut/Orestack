package io.github.pigaut.rpg.core.enchant;

import io.github.pigaut.rpg.util.reflection.*;
import org.bukkit.*;
import org.bukkit.enchantments.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class EnchantUtil {

    public static @NotNull String getEnchantName(@NotNull Enchantment enchantment) {
        return enchantment.getKey().getKey();
    }

    public static int getEnchantLevel(@NotNull ItemStack item, @NotNull List<String> enchantIds) {
        if (!item.hasItemMeta()) {
            return 0;
        }

        for (var enchantEntry : item.getItemMeta().getEnchants().entrySet()) {
            Enchantment enchant = enchantEntry.getKey();
            NamespacedKey enchantNamespace = enchant.getKey();
            if (!enchantIds.contains(enchantNamespace.getKey())) {
                continue;
            }
            return enchantEntry.getValue();
        }

        return 0;
    }

    private static final Map<String, Enchantment> ENCHANTMENTS = new HashMap<>();

    private static void registerEnchantment(String... names) {
        for (String name : names) {
            Enchantment foundEnchantment = Registry.ENCHANTMENT.get(NamespacedKey.minecraft(name));

            if (foundEnchantment != null) {
                for (String id : names) {
                    ENCHANTMENTS.put(id.toLowerCase(Locale.ENGLISH), foundEnchantment);
                }
            }
        }
    }

    static {
        registerEnchantment("aqua_affinity", "water_worker", "water_worker", "aqua_affinity", "water_mine");
        registerEnchantment("bane_of_arthropods", "bane_of_arthropods", "damage_arthropods", "bane_of_arthropod", "arthropod");
        registerEnchantment("binding_curse", "binding_curse", "bind_curse", "binding", "bind");
        registerEnchantment("blast_protection", "protection_explosions", "blast_protect", "explosions_protection", "explosion_protection", "blast_protection");
        registerEnchantment("breach", "breach");
        registerEnchantment("channeling", "channeling", "channelling", "chanelling", "chaneling", "channel");
        registerEnchantment("density", "density");
        registerEnchantment("depth_strider", "depth_strider", "depth", "strider");
        registerEnchantment("efficiency", "efficiency", "dig_speed", "mine_speed", "cut_speed");
        registerEnchantment("feather_falling", "protection_fall", "feather_fall", "fall_protection", "feather_falling");
        registerEnchantment("fire_aspect", "fire_aspect", "fire", "melee_fire", "melee_flame");
        registerEnchantment("fire_protection", "protection_fire", "fire_prot", "fire_protect", "fire_protection", "flame_protection", "flame_protect");
        registerEnchantment("flame", "flame", "arrow_fire", "flame_arrow", "fire_arrow");
        registerEnchantment("fortune", "fortune", "loot_bonus_blocks", "blocks_loot_bonus");
        registerEnchantment("frost_walker", "frost_walker", "frost", "walker");
        registerEnchantment("impaling", "impaling", "impale", "ocean_damage");
        registerEnchantment("infinity", "infinity", "arrow_infinite", "infinite_arrows", "infinite", "unlimited_arrows");
        registerEnchantment("knockback", "knockback");
        registerEnchantment("looting", "looting", "loot_bonus_mobs", "mob_loot", "mobs_loot_bonus");
        registerEnchantment("loyalty", "loyalty", "loyal", "return");
        registerEnchantment("luck_of_the_sea", "luck_of_the_sea", "luck", "luck_of_sea", "luck_of_seas", "rod_luck");
        registerEnchantment("lure", "lure", "rod_lure");
        registerEnchantment("mending", "mending");
        registerEnchantment("multishot", "multishot", "triple_shot");
        registerEnchantment("piercing", "piercing");
        registerEnchantment("power", "power", "arrow_damage", "arrow_power");
        registerEnchantment("projectile_protection", "protection_projectile", "projectile_protection");
        registerEnchantment("protection", "protection", "protection_environmental", "protect");
        registerEnchantment("punch", "punch", "arrow_knockback", "arrow_punch");
        registerEnchantment("quick_charge", "quick_charge", "quickcharge", "quick_draw", "fast_charge", "fast_draw");
        registerEnchantment("respiration", "respiration", "oxygen", "breath", "breathing");
        registerEnchantment("riptide", "riptide", "rip", "tide", "launch");
        registerEnchantment("sharpness", "sharpness", "damage_all", "all_damage", "all_dmg", "sharp");
        registerEnchantment("silk_touch", "silk_touch", "soft_touch");
        registerEnchantment("smite", "smite", "damage_undead", "undead_damage");
        registerEnchantment("soul_speed", "soul_speed", "speed_soul", "soul_runner");
        registerEnchantment("swift_sneak", "swift_sneak", "sneak_swift");
        registerEnchantment("thorns", "thorns", "highcrit", "thorn", "highercrit");
        registerEnchantment("unbreaking", "unbreaking", "durability", "dura");
        registerEnchantment("vanishing_curse", "vanishing_curse", "vanish_curse", "vanishing", "vanish");
        registerEnchantment("wind_burst", "wind_burst");
        registerEnchantment("sweeping_edge", "sweeping", "sweeping_edge", "sweep_edge");
    }

    public static @NotNull Enchantment getEnchantmentOrThrow(@NotNull String name) {
        Enchantment enchantment = getEnchantment(name);
        if (enchantment == null) {
            throw new IllegalStateException("Could not find enchantment with name: " + name);
        }
        return enchantment;
    }

    public static @Nullable Enchantment getEnchantment(@NotNull String name) {
        return ENCHANTMENTS.get(name.toLowerCase(Locale.ENGLISH));
    }

    public static boolean hasEnchant(@NotNull ItemStack item, @NotNull Enchantment enchantment) {
        if (!item.hasItemMeta()) {
            return false;
        }
        return item.getItemMeta().hasEnchant(enchantment);
    }

}
