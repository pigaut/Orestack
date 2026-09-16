package io.github.pigaut.rpg.module.structure.settings;

import io.github.pigaut.rpg.core.enchant.*;
import io.github.pigaut.rpg.module.structure.health.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.amount.*;
import io.github.pigaut.yaml.node.scalar.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class StructureConfigSettings implements StructureSettings {

    private final EnhancedPlugin plugin;
    private final Settings settings;

    public StructureConfigSettings(@NotNull EnhancedPlugin plugin, @NotNull Settings settings) {
        this.plugin = plugin;
        this.settings = settings;
    }

    private Boolean keepBlocksOnRemove;
    private Boolean restoreOriginalBlocksOnRemove;
    private Amount defaultDamage;
    private Boolean overflowDamage;
    private Boolean efficiencyDamageMultiplier;
    private Map<Integer, Double> efficiencyDamageMultiplierByLevel;
    private Boolean reducedCooldownDamage;
    private List<ToolDamage> damageByTool;

    public void loadConfiguration(@NotNull ConfigSection config) {
        keepBlocksOnRemove = config.getBoolean("keep-blocks-on-remove")
                .withDefault(false);

        restoreOriginalBlocksOnRemove = config.getBoolean("restore-original-blocks-on-remove")
                .withDefault(true);

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

        damageByTool = config.getList("structure-damage", ToolDamage.class)
                .withDefault(List.of());
    }

    @Override
    public boolean isKeepBlocksOnRemove() {
        settings.checkLoaded(keepBlocksOnRemove);
        return keepBlocksOnRemove;
    }

    @Override
    public boolean isRestoreBlocksOnRemove() {
        settings.checkLoaded(restoreOriginalBlocksOnRemove);
        return restoreOriginalBlocksOnRemove;
    }

    @Override
    public boolean isDamageOverflow() {
        settings.checkLoaded(overflowDamage);
        return overflowDamage;
    }

    @Override
    public boolean isEfficiencyDamageMultiplier() {
        settings.checkLoaded(efficiencyDamageMultiplier);
        return efficiencyDamageMultiplier;
    }

    @Override
    public boolean isReducedCooldownDamage() {
        settings.checkLoaded(reducedCooldownDamage);
        return reducedCooldownDamage;
    }

    @Override
    public @NotNull Amount getToolDamage(@NotNull Material toolType, @NotNull Material blockType) {
        settings.checkLoaded(damageByTool);
        settings.checkLoaded(defaultDamage);
        for (ToolDamage toolDamage : damageByTool) {
            if (toolDamage.test(toolType, blockType)) {
                return toolDamage.getDamage(toolType);
            }
        }
        return defaultDamage;
    }

    @Override
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
