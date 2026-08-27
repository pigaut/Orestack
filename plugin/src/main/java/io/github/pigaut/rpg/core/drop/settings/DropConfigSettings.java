package io.github.pigaut.rpg.core.drop.settings;

import io.github.pigaut.rpg.core.enchant.*;
import io.github.pigaut.rpg.module.function.action.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.amount.*;
import io.github.pigaut.yaml.node.*;
import io.github.pigaut.yaml.node.scalar.*;
import org.bukkit.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class DropConfigSettings implements DropSettings {

    private ItemDropTarget defaultItemDropTarget;

    private boolean silkTouch;
    private Map<Material, Material> silkDropByOriginal;

    private boolean fortune;
    private List<Material> fortuneDropsWhitelist;

    private boolean looting;
    private List<Material> lootingDropsWhitelist;

    private boolean autoSmelt;
    private List<String> autoSmeltAliases;
    private Map<Material, Material> smeltedDropByOriginal;
    private Map<Integer, Double> smeltChanceByLevel;

    private boolean telepathy;
    private List<String> telepathyAliases;
    private Map<Integer, Double> telepathyChanceByLevel;

    private boolean miningFortune;
    private List<Material> applyMiningFortune;

    private boolean experience;
    private List<String> experienceAliases;
    private Map<Integer, Amount> expMultiplierByLevel;

    public void loadConfiguration(@NotNull ConfigSection config) {
        defaultItemDropTarget = config.get("default-item-drop-target", ItemDropTarget.class)
                .withDefault(ItemDropTarget.BLOCK);

        silkTouch = config.getBoolean("silk-touch")
                .withDefault(true);

        silkDropByOriginal = new HashMap<>();
        for (KeyedField field : config.getSectionOrCreate("silk-touch-items").getNestedFields()) {
            Material originalDrop = field.getKeyAs(Material.class).withDefault(null);
            Material silkDrop = field.get(Material.class).withDefault(null);
            if (originalDrop != null && silkDrop != null) {
                silkDropByOriginal.put(originalDrop, silkDrop);
            }
        }

        fortune = config.getBoolean("fortune")
                .withDefault(true);

        fortuneDropsWhitelist = config.getList("apply-fortune|do-fortune-whitelist", Material.class)
                .withDefault(List.of());

        looting = config.getBoolean("looting")
                .withDefault(true);

        lootingDropsWhitelist = config.getList("apply-looting", Material.class)
                .withDefault(List.of());

        autoSmelt = config.getBoolean("auto-smelt")
                .withDefault(true);

        autoSmeltAliases = config.getStringList("auto-smelt-aliases")
                .withDefault(List.of());

        smeltedDropByOriginal = new HashMap<>();
        for (KeyedField field : config.getSectionOrCreate("smeltable-items").getNestedFields()) {
            Material original = field.getKeyAs(Material.class).withDefault(null);
            Material result = field.get(Material.class).withDefault(null);
            if (original != null && result != null) {
                smeltedDropByOriginal.put(original, result);
            }
        }

        smeltChanceByLevel = new HashMap<>();
        for (KeyedScalar scalar : config.getSectionOrCreate("smelt-chance-by-level").getNestedScalars()) {
            Integer level = scalar.getIntegerKey()
                    .require(Requirements.positive())
                    .withDefault(null);

            Double smeltChance = scalar.toDouble()
                    .require(Requirements.between(0, 1))
                    .withDefault(null);

            if (level != null && smeltChance != null) {
                smeltChanceByLevel.put(level, smeltChance);
            }
        }

        telepathy = config.getBoolean("telepathy")
                .withDefault(true);

        telepathyAliases = config.getStringList("telepathy-aliases")
                .withDefault(List.of());

        telepathyChanceByLevel = new HashMap<>();
        for (KeyedScalar scalar : config.getSectionOrCreate("telepathy-chance-by-level").getNestedScalars()) {
            Integer level = scalar.getIntegerKey()
                    .require(Requirements.positive())
                    .withDefault(null);

            Double telepathyChance = scalar.toDouble()
                    .require(Requirements.between(0, 1))
                    .withDefault(null);

            if (level != null && telepathyChance != null) {
                telepathyChanceByLevel.put(level, telepathyChance);
            }
        }

        miningFortune = config.getBoolean("mining-fortune")
                .withDefault(true);

        applyMiningFortune = config.getList("apply-mining-fortune", Material.class)
                .withDefault(List.of());

        experience = config.getBoolean("experience")
                .withDefault(true);

        experienceAliases = config.getStringList("experience-aliases")
                .withDefault(List.of());

        expMultiplierByLevel = new HashMap<>();
        for (KeyedScalar scalar : config.getSectionOrCreate("exp-multiplier-by-level").getNestedScalars()) {
            Integer level = scalar.getIntegerKey()
                    .require(Requirements.positive())
                    .withDefault(null);

            Amount expMultiplier = scalar.get(Amount.class)
                    .require(Requirements.positiveAmount())
                    .withDefault(null);

            if (level != null && expMultiplier != null) {
                expMultiplierByLevel.put(level, expMultiplier);
            }
        }
    }

    @Override
    public @NotNull ItemDropTarget getDefaultItemDropTarget() {
        return defaultItemDropTarget;
    }

    @Override
    public boolean isExperience() {
        return experience;
    }

    @Override
    public boolean isFortuneDrop(@NotNull Material material) {
        return fortune && fortuneDropsWhitelist.contains(material);
    }

    @Override
    public boolean isLootingDrop(@NotNull Material material) {
        return looting && lootingDropsWhitelist.contains(material);
    }

    @Override
    public boolean isAutoSmelt() {
        return autoSmelt;
    }

    @Override
    public boolean isMiningFortuneDrop(@NotNull Material material) {
        return miningFortune && applyMiningFortune.contains(material);
    }

    @Override
    public boolean isTelepathy() {
        return telepathy;
    }

    @Override
    public @Nullable Material getSilkDrop(@NotNull Material originalDrop) {
        return silkTouch ? silkDropByOriginal.get(originalDrop) : null;
    }

    @Override
    public @Nullable Material getSmeltedDrop(@NotNull Material originalDrop) {
        return autoSmelt ? smeltedDropByOriginal.get(originalDrop) : null;
    }

    @Override
    public double getSmeltChance(@NotNull ItemStack tool) {
        int enchantLevel = EnchantUtil.getEnchantLevel(tool, autoSmeltAliases);
        return smeltChanceByLevel.getOrDefault(enchantLevel, 0d);
    }

    @Override
    public double getTelepathyChance(@NotNull ItemStack tool) {
        int enchantLevel = EnchantUtil.getEnchantLevel(tool, telepathyAliases);
        return telepathyChanceByLevel.getOrDefault(enchantLevel, 0d);
    }

    @Override
    public @NotNull Amount getExperienceMultiplier(@NotNull ItemStack tool) {
        int enchantLevel = EnchantUtil.getEnchantLevel(tool, experienceAliases);
        return expMultiplierByLevel.getOrDefault(enchantLevel, Amount.ONE);
    }

}
