package io.github.pigaut.orestack.settings;

import io.github.pigaut.orestack.collection.*;
import io.github.pigaut.orestack.core.tools.*;
import io.github.pigaut.orestack.health.*;
import io.github.pigaut.voxel.bukkit.*;
import io.github.pigaut.voxel.core.enchant.*;
import io.github.pigaut.voxel.core.progressbar.*;
import io.github.pigaut.voxel.plugin.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.amount.*;
import io.github.pigaut.yaml.node.scalar.*;
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

    // Collections settings
    private List<CollectionSource> collectionSources;
    private ProgressBar collectionProgressBar;

    // VeinMiner settings
    private List<String> veinMinerAliases;
    private Map<Integer, Integer> veinSizeByLevel;

    // Gate settings
    private ItemStack gateTool;
    private int gateClickCooldown;

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
    public @NotNull List<ConfigException> loadConfigurationData() {
        List<ConfigException> errors = super.loadConfigurationData();

        ConfigSection config = plugin.getConfiguration();

        // Generic settings
        keepBlocksOnRemove = config.getBoolean("keep-blocks-on-remove")
                .withDefaultOrElse(false, errors::add);

        restoreOriginalBlocksOnRemove = config.getBoolean("restore-original-blocks-on-remove")
                .withDefaultOrElse(true, errors::add);

        // Collections settings
        collectionSources = config.getAll("collection-sources", CollectionSource.class)
                .withDefaultOrElse(List.of(), errors::add);

        collectionProgressBar = config.get("collection-progress-bar", ProgressBar.class)
                .withDefaultOrElse(ProgressBar.EMPTY, errors::add);

        // Generator settings
        generatorTool = config.get("generator-tool", ItemStack.class)
                .require(ItemUtil::isNotAir, "Item type cannot be air")
                .withDefaultOrElse(GeneratorTool.getItemTemplate(), errors::add);

        defaultToolDamage = config.get("default-tool-durability-damage|default-tool-damage", Amount.class)
                .withDefaultOrElse(Amount.ONE, errors::add);

        generatorClickCooldown = config.getInteger("generator-click-cooldown")
                .require(Requirements.positive())
                .withDefaultOrElse(4, errors::add);

        generatorHitCooldown = config.getInteger("generator-hit-cooldown")
                .require(Requirements.positive())
                .withDefaultOrElse(4, errors::add);

        generatorHarvestCooldown = config.getInteger("generator-harvest-cooldown")
                .require(Requirements.positive())
                .withDefaultOrElse(4, errors::add);

        // Vein miner settings
        veinMiner = config.getBoolean("vein-miner")
                .withDefaultOrElse(false, errors::add);

        veinMinerAliases = config.getStringList("vein-miner-aliases")
                .withDefaultOrElse(List.of("veinminer", "vein-miner", "vein_miner"), errors::add);

        veinSizeByLevel = new HashMap<>();
        for (KeyedScalar scalar : config.getSectionOrCreate("vein-size-by-level").getNestedScalars()) {
            Integer veinSize = scalar.toInteger()
                    .require(Requirements.positive())
                    .withDefaultOrElse(null, errors::add);

            Integer enchantLevel = scalar.getIntegerKey()
                    .require(Requirements.positive())
                    .withDefaultOrElse(null, errors::add);

            if (veinSize != null && enchantLevel != null) {
                veinSizeByLevel.put(enchantLevel, veinSize);
            }
        }

        // Gate settings
        gateTool = config.get("gate-tool", ItemStack.class)
                .require(ItemUtil::isNotAir, "Item type cannot be air")
                .withDefaultOrElse(GateTool.getItemTemplate(), errors::add);

        gateClickCooldown = config.getInteger("gate-click-cooldown")
                .require(Requirements.positive())
                .withDefaultOrElse(4, errors::add);

        // Health settings
        defaultDamage = config.get("default-damage", Amount.class)
                .withDefaultOrElse(Amount.ONE, errors::add);

        overflowDamage = config.getBoolean("overflow-damage")
                .withDefaultOrElse(true, errors::add);

        efficiencyDamageMultiplier = config.getBoolean("efficiency-damage-multiplier|efficiency-damage")
                .withDefaultOrElse(true, errors::add);

        efficiencyDamageMultiplierByLevel = new HashMap<>();
        for (KeyedScalar scalar : config.getSectionOrCreate("efficiency-damage-multiplier-by-level").getNestedScalars()) {
            Double damageMultiplier = scalar.toDouble()
                    .require(Requirements.positive())
                    .withDefaultOrElse(null, errors::add);

            Integer enchantLevel = scalar.getIntegerKey()
                    .require(Requirements.positive())
                    .withDefaultOrElse(null, errors::add);

            if (damageMultiplier != null && enchantLevel != null) {
                efficiencyDamageMultiplierByLevel.put(enchantLevel, damageMultiplier);
            }
        }

        reducedCooldownDamage = config.getBoolean("reduced-cooldown-damage")
                .withDefaultOrElse(true, errors::add);

        damageByTool = config.getList("damage-by-tool-type", ToolDamage.class)
                .withDefaultOrElse(List.of(), errors::add);

        return errors;
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

    public @NotNull List<CollectionSource> getCollectionSources() {
        return new ArrayList<>(collectionSources);
    }

    public boolean isCollectionSourceEnabled(@NotNull CollectionSource source) {
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

}
