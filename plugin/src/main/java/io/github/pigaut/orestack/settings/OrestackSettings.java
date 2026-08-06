package io.github.pigaut.orestack.settings;

import io.github.pigaut.orestack.core.tools.*;
import io.github.pigaut.orestack.health.*;
import io.github.pigaut.orestack.skill.exp.*;
import io.github.pigaut.voxel.bukkit.*;
import io.github.pigaut.voxel.core.enchant.*;
import io.github.pigaut.voxel.core.progressbar.*;
import io.github.pigaut.voxel.module.function.*;
import io.github.pigaut.voxel.event.drop.*;
import io.github.pigaut.voxel.plugin.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.amount.*;
import io.github.pigaut.yaml.node.scalar.*;
import io.github.pigaut.yaml.node.section.*;
import net.objecthunter.exp4j.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class OrestackSettings extends Settings {

    // Generic settings
    private boolean keepBlocksOnRemove;
    private boolean restoreOriginalBlocksOnRemove;

    // Generator settings
    private ItemStack generatorTool;
    private Amount defaultToolDamage;
    private int generatorClickCooldown;
    private int generatorHitCooldown;
    private int generatorHarvestCooldown;
    private boolean veinMiner;

    // VeinMiner settings
    private List<String> veinMinerAliases;
    private Map<Integer, Integer> veinSizeByLevel;

    // Gate settings
    private ItemStack gateTool;
    private int gateClickCooldown;

    // Collections settings
    private List<ItemSpawnReason> collectionSources;
    private ProgressBar collectionProgressBar;

    // Skill Settings
    private int defaultMaxLevel;
    private Expression defaultExpFormula;
    private Function defaultOnExpEarn;
    private Map<String, ExpAmount> expEarningActivities;
    private ProgressBar skillProgressBar;

    // Health settings
    private Amount defaultDamage;
    private boolean overflowDamage;
    private boolean efficiencyDamageMultiplier;
    private Map<Integer, Double> efficiencyDamageMultiplierByLevel;
    private boolean reducedCooldownDamage;
    private List<ToolDamage> damageByTool;

    public OrestackSettings(EnhancedPlugin plugin) {
        super(plugin);
    }

    @Override
    public @NotNull ErrorCollector loadConfigurationData() {
        super.loadConfigurationData();
        RootSection config = plugin.getConfiguration();

        // Generic settings
        keepBlocksOnRemove = config.getBoolean("keep-blocks-on-remove")
                .withDefault(false);

        restoreOriginalBlocksOnRemove = config.getBoolean("restore-original-blocks-on-remove")
                .withDefault(true);

        // Generator settings
        generatorTool = config.get("generator-tool", ItemStack.class)
                .require(ItemUtil::isNotAir, "Item type cannot be air")
                .withDefault(GeneratorTool.getItemTemplate());

        defaultToolDamage = config.get("default-tool-durability-damage|default-tool-damage", Amount.class)
                .withDefault(Amount.ONE);

        generatorClickCooldown = config.getInteger("generator-click-cooldown")
                .require(Requirements.positive())
                .withDefault(4);

        generatorHitCooldown = config.getInteger("generator-hit-cooldown")
                .require(Requirements.positive())
                .withDefault(4);

        generatorHarvestCooldown = config.getInteger("generator-harvest-cooldown")
                .require(Requirements.positive())
                .withDefault(4);

        // Vein miner settings
        veinMiner = config.getBoolean("vein-miner")
                .withDefault(false);

        veinMinerAliases = config.getStringList("vein-miner-aliases")
                .withDefault(List.of("veinminer", "vein-miner", "vein_miner"));

        veinSizeByLevel = new HashMap<>();
        for (KeyedScalar scalar : config.getSectionOrCreate("vein-size-by-level").getNestedScalars()) {
            Integer veinSize = scalar.toInteger()
                    .require(Requirements.positive())
                    .withDefault(null);

            Integer enchantLevel = scalar.getIntegerKey()
                    .require(Requirements.positive())
                    .withDefault(null);

            if (veinSize != null && enchantLevel != null) {
                veinSizeByLevel.put(enchantLevel, veinSize);
            }
        }

        // Gate settings
        gateTool = config.get("gate-tool", ItemStack.class)
                .require(ItemUtil::isNotAir, "Item type cannot be air")
                .withDefault(GateTool.getItemTemplate());

        gateClickCooldown = config.getInteger("gate-click-cooldown")
                .require(Requirements.positive())
                .withDefault(4);

        // Collections settings
        collectionSources = config.getAll("collection-item-sources", ItemSpawnReason.class)
                .withDefault(List.of());

        collectionProgressBar = config.get("collection-progress-bar", ProgressBar.class)
                .withDefault(ProgressBar.EMPTY);

        // Skills settings

        defaultMaxLevel = config.getInteger("default-skill-settings.max-level")
                .require(Requirements.positive())
                .withDefault(100);

        try {
            String rawFormula = config.getString("default-skill-settings.exp-formula")
                    .withDefault(null);
            defaultExpFormula = new ExpressionBuilder(Objects.requireNonNullElse(rawFormula, "(level/0.1)^2"))
                    .variable("level")
                    .build();
        } catch (Exception e) {
            config.collectError(new InvalidConfigException(config, "default-skill-settings.exp-formula", "Could not parse exp formula"));
            defaultExpFormula = new ExpressionBuilder("(level/0.1)^2")
                    .variable("level")
                    .build();
        }

        defaultOnExpEarn = config.get("default-skill-settings.on-exp-earn", Function.class)
                .withDefault(null);

        expEarningActivities = new HashMap<>();
        for (KeyedScalar scalar : config.getNestedScalars("exp-earning-activities")) {
            ConfigLine line = scalar.toLine();
            String name = scalar.getKey();
            ExpAmount expAmount = line.get(ExpAmount.class)
                    .withDefault(null);
            expEarningActivities.put(name, expAmount);
        }

        skillProgressBar = config.get("skill-progress-bar", ProgressBar.class)
                .withDefault(ProgressBar.EMPTY);

        // Health settings
        defaultDamage = config.get("default-damage", Amount.class)
                .withDefault(Amount.ONE);

        overflowDamage = config.getBoolean("overflow-damage")
                .withDefault(true);

        efficiencyDamageMultiplier = config.getBoolean("efficiency-damage-multiplier|efficiency-damage")
                .withDefault(true);

        efficiencyDamageMultiplierByLevel = new HashMap<>();
        for (KeyedScalar scalar : config.getSectionOrCreate("efficiency-damage-multiplier-by-level").getNestedScalars()) {
            Double damageMultiplier = scalar.toDouble()
                    .require(Requirements.positive())
                    .withDefault(null);

            Integer enchantLevel = scalar.getIntegerKey()
                    .require(Requirements.positive())
                    .withDefault(null);

            if (damageMultiplier != null && enchantLevel != null) {
                efficiencyDamageMultiplierByLevel.put(enchantLevel, damageMultiplier);
            }
        }

        reducedCooldownDamage = config.getBoolean("reduced-cooldown-damage")
                .withDefault(true);

        damageByTool = config.getList("damage-by-tool-type", ToolDamage.class)
                .withDefault(List.of());

        return config;
    }

    public boolean isKeepBlocksOnRemove() {
        return keepBlocksOnRemove;
    }

    public boolean isRestoreBlocksOnRemove() {
        return restoreOriginalBlocksOnRemove;
    }

    @NotNull
    public ItemStack getGeneratorTool() {
        return generatorTool.clone();
    }

    public ItemStack getGateTool() {
        return gateTool.clone();
    }

    public int getGateClickCooldown() {
        return gateClickCooldown;
    }

    public boolean isDamageOverflow() {
        return overflowDamage;
    }

    public boolean isEfficiencyDamageMultiplier() {
        return efficiencyDamageMultiplier;
    }

    public @NotNull List<ItemSpawnReason> getCollectionSources() {
        return new ArrayList<>(collectionSources);
    }

    public boolean isCollectionSourceEnabled(@NotNull ItemSpawnReason source) {
        return collectionSources.contains(source);
    }

    public @NotNull ProgressBar getCollectionProgressBar() {
        return collectionProgressBar;
    }

    public boolean isDefaultToolDamage() {
        return !defaultToolDamage.match(0);
    }

    public Amount getDefaultToolDamage() {
        return defaultToolDamage;
    }

    public int getGeneratorHitCooldown() {
        return generatorHitCooldown;
    }

    public int getGeneratorClickCooldown() {
        return generatorClickCooldown;
    }

    public int getGeneratorHarvestCooldown() {
        return generatorHarvestCooldown;
    }

    public boolean isReducedCooldownDamage() {
        return reducedCooldownDamage;
    }

    @NotNull
    public Amount getToolDamage(@NotNull Material toolType, @NotNull Material blockType) {
        for (ToolDamage toolDamage : damageByTool) {
            if (toolDamage.test(toolType, blockType)) {
                return toolDamage.getDamage(toolType);
            }
        }
        return defaultDamage;
    }

    public boolean isVeinMiner() {
        return veinMiner;
    }

    public int getToolMaxVeinSize(@NotNull ItemStack tool) {
        if (!tool.hasItemMeta()) {
            return 1;
        }

        int enchantLevel = EnchantUtil.getEnchantLevel(tool, veinMinerAliases);
        return veinSizeByLevel.getOrDefault(enchantLevel, 1);
    }

    public double getStructureDamage(@NotNull Player player, @NotNull Block block) {
        ItemStack tool = player.getInventory().getItemInMainHand();
        Amount damageAmount = getToolDamage(tool.getType(), block.getType());
        double damage = damageAmount.doubleValue();

        if (isEfficiencyDamageMultiplier()) {
            int efficiencyLevel = tool.getEnchantmentLevel(Enchants.EFFICIENCY);
            Double damageMultiplier = efficiencyDamageMultiplierByLevel.get(efficiencyLevel);
            if (damageMultiplier != null) {
                damage *= damageMultiplier;
            }
        }

        if (isReducedCooldownDamage()) {
            damage *= player.getAttackCooldown();
        }

        return damage;
    }

    public int getDefaultMaxSkillLevel() {
        return defaultMaxLevel;
    }

    public @NotNull Expression getDefaultExpFormula() {
        return defaultExpFormula;
    }

    public @Nullable Function getDefaultOnExpEarn() {
        return defaultOnExpEarn;
    }

    public @Nullable ExpAmount getExpEarningActivity(@NotNull String name) {
        return expEarningActivities.get(name);
    }

    public @NotNull ProgressBar getSkillProgressBar() {
        return skillProgressBar;
    }

}
