package io.github.pigaut.rpg.bukkit;

import io.github.pigaut.rpg.bukkit.material.*;
import io.github.pigaut.rpg.core.enchant.*;
import io.github.pigaut.rpg.bukkit.material.*;
import io.github.pigaut.rpg.core.enchant.*;
import io.github.pigaut.rpg.server.Server;
import io.github.pigaut.rpg.server.version.*;
import io.github.pigaut.rpg.util.reflection.*;
import io.github.pigaut.rpg.server.version.*;
import io.github.pigaut.rpg.util.reflection.*;
import io.github.pigaut.yaml.util.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.components.*;
import org.jetbrains.annotations.*;

import java.util.*;
import java.util.concurrent.*;

public class ItemUtil {

    public static boolean isAir(@NotNull ItemStack item) {
        return MaterialUtil.isAir(item.getType());
    }

    public static boolean isNotAir(@NotNull ItemStack item) {
        return MaterialUtil.isNotAir(item.getType());
    }

    public static void dropItem(@NotNull Location location, @NotNull ItemStack item) {
        World world = location.getWorld();
        if (world == null) {
            world = Server.getDefaultWorld();
        }
        world.dropItemNaturally(location, item);
    }

    public static void dropItem(@NotNull Location location, @NotNull ItemStack item, int amount) {
        World world = location.getWorld();
        if (world == null) {
            world = Server.getDefaultWorld();
        }
        ItemStack drop = item.clone();
        drop.setAmount(amount);
        world.dropItemNaturally(location, drop);
    }

    public static void dropItems(@NotNull Location location, @NotNull ItemStack... items) {
        World world = location.getWorld();
        if (world == null) {
            world = Server.getDefaultWorld();
        }

        for (ItemStack drop : items) {
            world.dropItemNaturally(location, drop);
        }
    }

    public static void damagePlayerTool(@NotNull Player player, int amount) {
        damageItem(player.getInventory().getItemInMainHand(), player, amount);
    }

    public static void setGlowing(@NotNull ItemStack item, boolean glowing) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return;
        }

        if (Server.getVersion() >= Version.V1_20_5) {
            meta.setEnchantmentGlintOverride(glowing);
            item.setItemMeta(meta);
            return;
        }

        if (glowing) {
            if (!meta.hasEnchants()) {
                meta.addEnchant(Enchants.LUCK, 1, true);
            }
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            item.setItemMeta(meta);
        }
    }

    private static final boolean MAX_DAMAGE_EXISTS = Reflect.onClass(Damageable.class).matchMethod("hasMaxDamage");

    public static void damageItem(@NotNull ItemStack item, @NotNull Player player, int amount) {
        Preconditions.checkArgument(amount >= 0, "Damage amount must be positive");
        Material material = item.getType();
        if (!material.isItem()) {
            return;
        }

        ItemMeta meta = item.getItemMeta();
        if (!(meta instanceof Damageable damageable)) {
            return;
        }

        if (meta.isUnbreakable()) {
            return;
        }

        int maxDamage = MAX_DAMAGE_EXISTS && damageable.hasMaxDamage() ? damageable.getMaxDamage() : material.getMaxDurability();
        if (maxDamage < 1) {
            return;
        }

        int damageAmount = amount;
        if (meta.hasEnchant(Enchants.UNBREAKING)) {
            int unbreakingLevel = meta.getEnchantLevel(Enchants.UNBREAKING);
            damageAmount = 0;
            Random random = ThreadLocalRandom.current();
            for (int i = 0; i < amount; i++) {
                if (random.nextInt(unbreakingLevel + 1) == 0) {
                    damageAmount++;
                }
            }
        }

        int damageToApply = (damageable.hasDamage() ? damageable.getDamage() : 0) + damageAmount;
        if (damageToApply >= maxDamage) {
            item.setAmount(0);
            player.playSound(player.getLocation(), "entity.item.break", 1f, 1f);
        }
        else {
            damageable.setDamage(damageToApply);
            item.setItemMeta((ItemMeta) damageable); // Cast for 1.16.5, Damageable does not extend ItemMeta
        }
    }

    private static final Map<Material, Float> MINING_SPEEDS = new HashMap<>();

    static {
        MINING_SPEEDS.put(Material.AIR, 1f);

        MINING_SPEEDS.put(Material.WOODEN_PICKAXE, 2f);
        MINING_SPEEDS.put(Material.WOODEN_AXE, 2f);
        MINING_SPEEDS.put(Material.WOODEN_SHOVEL, 2f);
        MINING_SPEEDS.put(Material.WOODEN_HOE, 2f);

        MINING_SPEEDS.put(Material.STONE_PICKAXE, 4f);
        MINING_SPEEDS.put(Material.STONE_AXE, 4f);
        MINING_SPEEDS.put(Material.STONE_SHOVEL, 4f);
        MINING_SPEEDS.put(Material.STONE_HOE, 4f);

        if (Server.getVersion() > Version.V1_21_9) {
            MINING_SPEEDS.put(Material.COPPER_PICKAXE, 5f);
            MINING_SPEEDS.put(Material.COPPER_AXE, 5f);
            MINING_SPEEDS.put(Material.COPPER_SHOVEL, 5f);
            MINING_SPEEDS.put(Material.COPPER_HOE, 5f);
        }

        MINING_SPEEDS.put(Material.IRON_PICKAXE, 6f);
        MINING_SPEEDS.put(Material.IRON_AXE, 6f);
        MINING_SPEEDS.put(Material.IRON_SHOVEL, 6f);
        MINING_SPEEDS.put(Material.IRON_HOE, 6f);

        MINING_SPEEDS.put(Material.DIAMOND_PICKAXE, 8f);
        MINING_SPEEDS.put(Material.DIAMOND_AXE, 8f);
        MINING_SPEEDS.put(Material.DIAMOND_SHOVEL, 8f);
        MINING_SPEEDS.put(Material.DIAMOND_HOE, 8f);

        MINING_SPEEDS.put(Material.NETHERITE_PICKAXE, 9f);
        MINING_SPEEDS.put(Material.NETHERITE_AXE, 9f);
        MINING_SPEEDS.put(Material.NETHERITE_SHOVEL, 9f);
        MINING_SPEEDS.put(Material.NETHERITE_HOE, 9f);

        MINING_SPEEDS.put(Material.GOLDEN_PICKAXE, 12f);
        MINING_SPEEDS.put(Material.GOLDEN_AXE, 12f);
        MINING_SPEEDS.put(Material.GOLDEN_SHOVEL, 12f);
        MINING_SPEEDS.put(Material.GOLDEN_HOE, 12f);
    }

    public static float getMiningSpeed(@NotNull ItemStack item, @NotNull Material block) {
        if (Server.getVersion() >= Version.V1_20_6) {
            if (item.hasItemMeta()) {
                ItemMeta meta = item.getItemMeta();
                if (meta.hasTool()) {
                    ToolComponent tool = meta.getTool();
                    for (ToolComponent.ToolRule rule : tool.getRules()) {
                        if (rule.getBlocks().contains(block)) {
                            return rule.getSpeed();
                        }
                    }
                    return tool.getDefaultMiningSpeed();
                }
            }
        }

        Material toolType = item.getType();
        Float miningSpeed = MINING_SPEEDS.get(toolType);
        if (miningSpeed != null) {
            return miningSpeed;
        }

        if (Tag.ITEMS_SWORDS.isTagged(toolType)) {
            if (block == Material.COBWEB) {
                return 15;
            }
            if (block == Material.BAMBOO) {
                return 30;
            }
            return 1.5f;
        }

        if (toolType == Material.SHEARS) {
            if (Tag.WOOL.isTagged(block)) {
                return 5;
            }
            if (Tag.WOOL_CARPETS.isTagged(block)) {
                return 5;
            }
            if (block == Material.COBWEB) {
                return 15;
            }
            return 2;
        }

        return 1;
    }

}
