package io.github.pigaut.rpg.bukkit.material;

import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.bukkit.attribute.*;
import io.github.pigaut.rpg.bukkit.effect.*;
import io.github.pigaut.rpg.core.enchant.*;
import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.bukkit.attribute.*;
import io.github.pigaut.rpg.bukkit.effect.*;
import io.github.pigaut.rpg.core.enchant.*;
import io.github.pigaut.rpg.server.Server;
import io.github.pigaut.rpg.server.version.*;
import io.github.pigaut.rpg.server.version.*;
import io.github.pigaut.yaml.convert.format.*;
import io.github.pigaut.yaml.util.*;
import org.bukkit.*;
import org.bukkit.block.data.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

import java.util.*;

import static org.bukkit.Material.*;

public class MaterialUtil {

    public static <T extends BlockData> boolean isBlockData(@NotNull Material material, @NotNull Class<T> blockData) {
        return material.isBlock() && blockData.isInstance(material.createBlockData());
    }

    public static boolean isAir(@NotNull Material material) {
        return material == Material.AIR ||
                material == Material.CAVE_AIR ||
                material == Material.VOID_AIR;
    }

    public static boolean isNotAir(@NotNull Material material) {
        return !isAir(material);
    }

    public static @NotNull Material getEntitySpawnEgg(@NotNull EntityType entityType) {
        Material foundSpawnEgg = Material.matchMaterial(entityType.name() + "_SPAWN_EGG");
        return foundSpawnEgg != null ? foundSpawnEgg : PIG_SPAWN_EGG;
    }

    private static final List<Material> CROPS = new ArrayList<>();
    private static final Map<Material, Material> CROP_SEEDS = new HashMap<>();
    private static final Map<Material, Material> CROP_ITEMS = new HashMap<>();

    static {
        CROPS.add(WHEAT);
        CROPS.add(CARROTS);
        CROPS.add(POTATOES);
        CROPS.add(BEETROOTS);
        CROPS.add(NETHER_WART);
        CROPS.add(CHORUS_FRUIT);
        CROPS.add(COCOA);
        CROPS.add(SWEET_BERRY_BUSH);
        CROPS.add(PUMPKIN_STEM);
        CROPS.add(MELON_STEM);
        CROPS.add(SUGAR_CANE);
        CROPS.add(CACTUS);
        CROPS.add(BAMBOO);

        CROP_SEEDS.put(WHEAT, WHEAT_SEEDS);
        CROP_SEEDS.put(CARROTS, CARROT);
        CROP_SEEDS.put(POTATOES, POTATO);
        CROP_SEEDS.put(BEETROOTS, BEETROOT_SEEDS);
        CROP_SEEDS.put(NETHER_WART, NETHER_WART);
        CROP_SEEDS.put(CHORUS_FRUIT, CHORUS_PLANT);
        CROP_SEEDS.put(COCOA, COCOA_BEANS);
        CROP_SEEDS.put(SWEET_BERRY_BUSH, SWEET_BERRIES);
        CROP_SEEDS.put(PUMPKIN_STEM, PUMPKIN_SEEDS);
        CROP_SEEDS.put(MELON_STEM, MELON_SEEDS);
        CROP_SEEDS.put(SUGAR_CANE, SUGAR_CANE);
        CROP_SEEDS.put(CACTUS, CACTUS);
        CROP_SEEDS.put(BAMBOO, BAMBOO);

        CROP_ITEMS.put(CARROTS, CARROT);
        CROP_ITEMS.put(POTATOES, POTATO);
        CROP_ITEMS.put(BEETROOTS, BEETROOT);
        CROP_ITEMS.put(COCOA, COCOA_BEANS);
        CROP_ITEMS.put(PUMPKIN_STEM, PUMPKIN);
        CROP_ITEMS.put(MELON_STEM, MELON);

        if (Server.getVersion() >= Version.V1_17) {
            CROPS.add(CAVE_VINES);
            CROPS.add(CAVE_VINES_PLANT);
            CROP_SEEDS.put(CAVE_VINES, VINE);
            CROP_SEEDS.put(CAVE_VINES_PLANT, VINE);
        }
    }

    public static boolean isCrop(@NotNull Material material) {
        return CROPS.contains(material);
    }

    public static @NotNull Material getCropItem(@NotNull Material crop) {
        Preconditions.checkArgument(isCrop(crop), "Material is not a crop");
        return CROP_ITEMS.getOrDefault(crop, crop);
    }

    public static @NotNull Material getCropSeeds(@NotNull Material crop) {
        Preconditions.checkArgument(isCrop(crop), "Material is not a crop");
        return CROP_SEEDS.get(crop);
    }

    public static boolean isInteractable(@NotNull Material material) {
        if (!material.isInteractable()) {
            return false;
        }

        return !Tag.STAIRS.isTagged(material) && !Tag.FENCES.isTagged(material);
    }

    public static @Nullable Material getMaterial(@NotNull String name) {
        name = CaseFormatter.toConstantCase(name);
        Material material = Material.getMaterial(name);
        if (material != null) {
            return material;
        }
        return CHANGED_MATERIAL_NAMES.get(name);
    }

    private static final Map<String, Material> CHANGED_MATERIAL_NAMES = new HashMap<>();

    private static void registerChangedName(int version, String oldName, String newName) {
        if (Server.getVersion() >= version) {
            Material newMaterial = Material.getMaterial(newName);
            if (newMaterial == null) {
                throw new IllegalStateException("Could not register new material: " + newName + " under old name: " + oldName);
            }
            CHANGED_MATERIAL_NAMES.put(oldName, newMaterial);
        } else {
            Material newMaterial = Material.getMaterial(oldName);
            if (newMaterial == null) {
                throw new IllegalStateException("Could not register old material: " + oldName + " under new name: " + newName);
            }
            CHANGED_MATERIAL_NAMES.put(newName, newMaterial);
        }
    }

    private static void registerMaterialAlias(String alias, Material material) {
        CHANGED_MATERIAL_NAMES.put(alias, material);
    }

    static {
        if (Server.getVersion() != Version.UNKNOWN) {
            registerChangedName(Version.V1_17, "GRASS_PATH", "DIRT_PATH");
            registerChangedName(Version.V1_20_4, "GRASS", "SHORT_GRASS");
            registerChangedName(Version.V1_20_5, "SCUTE", "TURTLE_SCUTE");
            registerChangedName(Version.V1_21_10, "CHAIN", "IRON_CHAIN");
        }

        registerMaterialAlias("GOLD_PICKAXE", GOLDEN_PICKAXE);
        registerMaterialAlias("GOLD_AXE", GOLDEN_AXE);
        registerMaterialAlias("GOLD_SHOVEL", GOLDEN_SHOVEL);
        registerMaterialAlias("GOLD_HOE", GOLDEN_HOE);
        registerMaterialAlias("GOLD_SWORD", GOLDEN_SWORD);
        registerMaterialAlias("GOLD_HELMET", GOLDEN_HELMET);
        registerMaterialAlias("GOLD_CHESTPLATE", GOLDEN_CHESTPLATE);
        registerMaterialAlias("GOLD_LEGGINGS", GOLDEN_LEGGINGS);
        registerMaterialAlias("GOLD_BOOTS", GOLDEN_BOOTS);
        registerMaterialAlias("GOLD_CARROT", GOLDEN_CARROT);
        registerMaterialAlias("SEEDS", WHEAT_SEEDS);
        registerMaterialAlias("RAW_IRON", IRON_ORE);
        registerMaterialAlias("RAW_GOLD", GOLD_ORE);
        registerMaterialAlias("HEAD", PLAYER_HEAD);
    }

    private static final boolean newerThan1_21 = Server.getVersion() >= Version.V1_21;

    public static long calculateBreakTime(@NotNull Player player, @NotNull BlockData blockData) {
        Preconditions.checkState(Server.getVersion() >= Version.V1_19_3, "Break time can only be calculated on servers 1.19.3+");
        Material block = blockData.getMaterial();
        float hardness = block.getHardness();
        if (hardness <= 0) {
            return 0;
        }

        ItemStack tool = player.getInventory().getItemInMainHand();
        double speedMultiplier = ItemUtil.getMiningSpeed(tool, block);

        if (speedMultiplier > 1) {
            if (newerThan1_21) {
                Double miningEfficiency = PlayerUtil.getAttributeAmount(player, Attributes.MINING_EFFICIENCY);
                if (miningEfficiency != null) {
                    speedMultiplier += miningEfficiency;
                }
            }
            else {
                int efficiencyLevel = tool.getEnchantmentLevel(Enchants.EFFICIENCY);
                if (efficiencyLevel > 0) {
                    speedMultiplier += Math.pow(efficiencyLevel, 2) + 1;
                }
            }
        }

        // Haste
        int hasteLevel = PlayerUtil.getEffectLevel(player, PotionEffects.HASTE);
        int conduitLevel = PlayerUtil.getEffectLevel(player, PotionEffects.CONDUIT_POWER);
        int maxHaste = Math.max(hasteLevel, conduitLevel);
        if (maxHaste > 0) {
            speedMultiplier *= (0.2 * maxHaste) + 1;
        }

        // Fatigue
        int fatigueLevel = PlayerUtil.getEffectLevel(player, PotionEffects.MINING_FATIGUE);
        if (fatigueLevel > 0) {
            switch (fatigueLevel) {
                case 1 -> speedMultiplier *= 0.3;
                case 2 -> speedMultiplier *= 0.09;
                case 3 -> speedMultiplier *= 0.0027;
                default -> speedMultiplier *= 0.00081;
            }
        }

        if (newerThan1_21) {
            Double miningSpeed = PlayerUtil.getAttributeAmount(player, Attributes.MINING_SPEED);
            if (miningSpeed != null) {
                speedMultiplier *= miningSpeed;
            }
        }

        if (player.isInWater()) {
            if (newerThan1_21) {
                Double submergedMiningSpeed = PlayerUtil.getAttributeAmount(player, Attributes.SUBMERGED_MINING_SPEED);
                if (submergedMiningSpeed != null) {
                    speedMultiplier *= submergedMiningSpeed;
                }
            }
            else {
                ItemStack helmet = player.getInventory().getHelmet();
                if (helmet != null && !EnchantUtil.hasEnchant(helmet, Enchants.AQUA_AFFINITY)) {
                    speedMultiplier /= 5.0;
                }
            }
        }

        if (!PlayerUtil.isOnGround(player)) {
            speedMultiplier /= 5.0;
        }

        boolean preferredTool = blockData.isPreferredTool(tool);
        double damage = speedMultiplier / (hardness * (preferredTool ? 30f : 100f));

        if (damage >= 1.0f) {
            return 0;
        }

        int ticks = (int) Math.ceil(1.0 / damage);
        return (long) ticks * 50L;
    }

}
