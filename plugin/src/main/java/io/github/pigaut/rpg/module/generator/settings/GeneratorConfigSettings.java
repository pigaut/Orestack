package io.github.pigaut.rpg.module.generator.settings;

import io.github.pigaut.rpg.core.enchant.*;
import io.github.pigaut.rpg.module.generator.*;
import io.github.pigaut.rpg.module.generator.template.*;
import io.github.pigaut.rpg.module.structure.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.amount.*;
import io.github.pigaut.yaml.delay.*;
import io.github.pigaut.yaml.node.scalar.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class GeneratorConfigSettings implements GeneratorSettings {

    private final EnhancedPlugin plugin;
    private final Settings settings;

    public GeneratorConfigSettings(@NotNull EnhancedPlugin plugin, @NotNull Settings settings) {
        this.plugin = plugin;
        this.settings = settings;
    }

    private Amount defaultToolDamage;
    private Delay generatorClickCooldown;
    private Delay generatorHitCooldown;
    private Delay generatorHarvestCooldown;

    private Boolean veinMiner;
    private List<String> veinMinerAliases;
    private Map<Integer, Integer> veinSizeByLevel;

    private List<GeneratorTemplate> veinGenerators;
    private Map<GeneratorTemplate, StructureTemplate> virtualGeneratorsBarrierLayouts;

    public void loadConfiguration(@NotNull ConfigSection config) {
        defaultToolDamage = config.get("default-tool-durability-damage|default-tool-damage", Amount.class)
                .withDefault(Amount.ONE);

        generatorClickCooldown = config.get("generator-click-cooldown", Delay.class)
                .withDefault(Delay.fromTicks(4));

        generatorHitCooldown = config.get("generator-hit-cooldown", Delay.class)
                .withDefault(Delay.fromTicks(4));

        generatorHarvestCooldown = config.get("generator-harvest-cooldown", Delay.class)
                .withDefault(Delay.fromTicks(20));

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

        plugin.loadWhenReady(() -> {
            veinGenerators = config.getList("vein-generators", GeneratorTemplate.class)
                    .withDefault(List.of());

            virtualGeneratorsBarrierLayouts = new HashMap<>();
            for (KeyedScalar scalar : config.getSectionOrEmpty("per-player-generators-barrier-layouts").getNestedScalars()) {
                GeneratorTemplate generatorTemplate = scalar.getKeyAs(GeneratorTemplate.class)
                        .withDefault(null);

                StructureTemplate barrierLayout;
                if (scalar.equalsIgnoreCase("none")) {
                    barrierLayout = StructureTemplate.createEmpty(plugin);
                } else {
                    barrierLayout = scalar.get(StructureTemplate.class)
                            .withDefault(null);
                }

                if (generatorTemplate != null && barrierLayout != null) {
                    virtualGeneratorsBarrierLayouts.put(generatorTemplate, barrierLayout);
                }
            }
        });
    }

    @Override
    public boolean isVeinMiner() {
        settings.checkLoaded(veinMiner);
        return veinMiner;
    }

    @Override
    public int getToolMaxVeinSize(@NotNull ItemStack tool) {
        settings.checkLoaded(veinSizeByLevel);
        if (!tool.hasItemMeta()) {
            return 1;
        }

        int enchantLevel = EnchantUtil.getEnchantLevel(tool, veinMinerAliases);
        return veinSizeByLevel.getOrDefault(enchantLevel, 1);
    }

    @Override
    public boolean isVeinGenerator(@NotNull Generator generator) {
        settings.checkLoaded(veinGenerators);
        return veinGenerators.contains(generator.getTemplate());
    }

    @Override
    public @Nullable StructureTemplate getVirtualGeneratorBarrierLayout(@NotNull GeneratorTemplate generatorTemplate) {
        settings.checkLoaded(virtualGeneratorsBarrierLayouts);
        return virtualGeneratorsBarrierLayouts.get(generatorTemplate);
    }

    @Override
    public boolean isDefaultToolDamage() {
        settings.checkLoaded(defaultToolDamage);
        return !defaultToolDamage.match(0);
    }

    @Override
    public Amount getDefaultToolDamage() {
        settings.checkLoaded(defaultToolDamage);
        return defaultToolDamage;
    }

    @Override
    public @NotNull Delay getGeneratorHitCooldown() {
        settings.checkLoaded(generatorHitCooldown);
        return generatorHitCooldown;
    }

    @Override
    public @NotNull Delay getGeneratorClickCooldown() {
        settings.checkLoaded(generatorClickCooldown);
        return generatorClickCooldown;
    }

    @Override
    public @NotNull Delay getGeneratorHarvestCooldown() {
        settings.checkLoaded(generatorHarvestCooldown);
        return generatorHarvestCooldown;
    }

}
