package io.github.pigaut.orestack.options;

import io.github.pigaut.orestack.damage.*;
import io.github.pigaut.orestack.util.*;
import io.github.pigaut.voxel.plugin.*;
import io.github.pigaut.voxel.plugin.manager.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.amount.*;
import org.bukkit.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class OrestackOptionsManager extends Manager implements ConfigBacked {

    private boolean keepBlocksOnRemove;
    private ItemStack generatorTool;

    // Cooldowns
    private int clickCooldown;
    private int hitCooldown;
    private int harvestCooldown;

    // Generator health options
    private Amount defaultDamage;
    private boolean efficiencyDamage;
    private boolean reducedCooldownDamage;
    private List<ToolDamage> damageByTool;

    public OrestackOptionsManager(EnhancedJavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public @NotNull List<ConfigurationException> loadConfigurationData() {
        List<ConfigurationException> errorsFound = new ArrayList<>();

        ConfigSection config = plugin.getConfiguration();

        keepBlocksOnRemove = config.getBoolean("keep-blocks-on-remove")
                .withDefault(false, errorsFound::add);

        generatorTool = config.get("generator-tool", ItemStack.class)
                .withDefault(GeneratorTool.getDefaultItem(), errorsFound::add);

        // Generator cooldowns
        clickCooldown = config.getInteger("click-cooldown")
                .withDefault(4, errorsFound::add);

        hitCooldown = config.getInteger("hit-cooldown")
                .withDefault(4, errorsFound::add);

        harvestCooldown = config.getInteger("harvest-cooldown")
                .withDefault(4, errorsFound::add);

        // Generator health options
        defaultDamage = config.get("default-damage", Amount.class)
                .withDefault(Amount.ONE, errorsFound::add);

        efficiencyDamage = config.getBoolean("efficiency-damage")
                .withDefault(true, errorsFound::add);

        reducedCooldownDamage = config.getBoolean("reduced-cooldown-damage")
                .withDefault(true, errorsFound::add);

        damageByTool = config.getElements("damage-by-tool-type", ToolDamage.class)
                .withDefault(List.of(), errorsFound::add);

        return errorsFound;
    }

    public boolean isKeepBlocksOnRemove() {
        return keepBlocksOnRemove;
    }

    public @NotNull ItemStack getGeneratorTool() {
        return generatorTool.clone();
    }

    public boolean isEfficiencyDamage() {
        return efficiencyDamage;
    }

    public int getHitCooldown() {
        return hitCooldown;
    }

    public int getClickCooldown() {
        return clickCooldown;
    }

    public int getHarvestCooldown() {
        return harvestCooldown;
    }

    public boolean isReducedCooldownDamage() {
        return reducedCooldownDamage;
    }

    public @NotNull Amount getToolDamage(@NotNull Material toolType, @NotNull Material blockType) {
        for (ToolDamage toolDamage : damageByTool) {
            if (toolDamage.test(toolType, blockType)) {
                return toolDamage.getDamage(toolType);
            }
        }
        return defaultDamage;
    }

}
