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

    private boolean keepBlocksOnRemove;
    private boolean restoreOriginalBlocksOnRemove;
    private Amount defaultDamage;
    private boolean overflowDamage;
    private boolean efficiencyDamageMultiplier;
    private Map<Integer, Double> efficiencyDamageMultiplierByLevel;
    private boolean reducedCooldownDamage;
    private List<ToolDamage> damageByTool;

    public StructureConfigSettings(EnhancedPlugin plugin) {
        this.plugin = plugin;
    }

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
        return keepBlocksOnRemove;
    }

    @Override
    public boolean isRestoreBlocksOnRemove() {
        return restoreOriginalBlocksOnRemove;
    }

    @Override
    public boolean isDamageOverflow() {
        return overflowDamage;
    }

    @Override
    public boolean isEfficiencyDamageMultiplier() {
        return efficiencyDamageMultiplier;
    }

    @Override
    public boolean isReducedCooldownDamage() {
        return reducedCooldownDamage;
    }

    @Override
    public @NotNull Amount getToolDamage(@NotNull Material toolType, @NotNull Material blockType) {
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
