package io.github.pigaut.orestack.options;

import io.github.pigaut.orestack.damage.*;
import io.github.pigaut.orestack.util.*;
import io.github.pigaut.voxel.plugin.*;
import io.github.pigaut.voxel.plugin.boot.*;
import io.github.pigaut.voxel.plugin.manager.*;
import io.github.pigaut.voxel.util.reflection.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.amount.*;
import io.github.pigaut.yaml.node.scalar.*;
import io.github.pigaut.yaml.util.*;
import org.bukkit.*;
import org.bukkit.enchantments.*;
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

    // VeinMiner options
    private boolean veinMiner;
    private List<String> veinMinerAliases;
    private Map<Integer, Integer> veinSizeByLevel;

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

        // Vein miner options
        veinMiner = config.getBoolean("vein-miner")
                .withDefault(false, errorsFound::add);

        veinMinerAliases = config.getStrings("vein-miner-aliases")
                .withDefault(List.of("vein-miner", "veinminer"), errorsFound::add);

        veinSizeByLevel = new HashMap<>();
        for (ConfigScalar scalar : config.getSectionOrCreate("vein-size-by-level").getNestedScalars()) {
            Integer veinSize = scalar.toInteger()
                    .filter(Predicates.isPositive(), "Vein size must be positive")
                    .withDefault(null, errorsFound::add);

            Integer enchantLevel = ((KeyedScalar) scalar).getIntegerKey()
                    .filter(Predicates.isPositive(), "Enchant level must be positive")
                    .withDefault(null, errorsFound::add);

            if (veinSize != null && enchantLevel != null) {
                veinSizeByLevel.put(enchantLevel, veinSize);
            }
        }

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

    public boolean isVeinMiner() {
        return veinMiner;
    }

    private final boolean spigotEnchants = Reflection.onClass(Enchantment.class)
            .matchMethod("getKeyOrNull");

    public int getToolMaxVeinSize(@NotNull ItemStack tool) {
        if (!tool.hasItemMeta()) {
            return 1;
        }

        for (Map.Entry<Enchantment, Integer> enchantToLevel : tool.getItemMeta().getEnchants().entrySet()) {
            Enchantment enchant = enchantToLevel.getKey();

            NamespacedKey enchantKey;
            if (spigotEnchants) {
                enchantKey = enchant.getKeyOrNull();
            }
            else {
                enchantKey = enchant.getKey();
            }

            if (enchantKey == null) {
                continue;
            }

            if (!veinMinerAliases.contains(enchantKey.getKey())) {
                continue;
            }

            return veinSizeByLevel.getOrDefault(enchantToLevel.getValue(), 1);
        }

        return 1;
    }

}
