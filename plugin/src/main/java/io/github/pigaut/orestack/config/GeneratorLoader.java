package io.github.pigaut.orestack.config;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.generator.*;
import io.github.pigaut.orestack.generator.template.*;
import io.github.pigaut.voxel.core.function.*;
import io.github.pigaut.voxel.core.hologram.*;
import io.github.pigaut.voxel.core.structure.*;
import io.github.pigaut.voxel.plugin.manager.*;
import io.github.pigaut.voxel.server.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.configurator.load.*;
import io.github.pigaut.yaml.util.*;
import org.bukkit.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class GeneratorLoader implements ConfigLoader<GeneratorTemplate> {

    private final OrestackPlugin plugin;

    public GeneratorLoader(OrestackPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getProblemDescription() {
        return "invalid generator";
    }

    @Override
    public @NotNull GeneratorTemplate loadFromSequence(@NotNull ConfigSequence sequence) throws InvalidConfigurationException {
        if (!(sequence instanceof ConfigRoot root)) {
            throw new InvalidConfigurationException(sequence, "Generator can only be loaded from a root configuration sequence");
        }

        final String name = root.getName();
        final String group = Group.byFile(root.getFile(), "generators", true);
        final List<GeneratorStage> generatorStages = new ArrayList<>();
        final GeneratorTemplate generator = new GeneratorTemplate(name, group, generatorStages);
        for (ConfigSection nestedSection : sequence.getNestedSections()) {
            generatorStages.add(loadStage(generator, nestedSection));
        }

        if (generatorStages.size() < 2) {
            throw new InvalidConfigurationException(sequence, "Generator must have at least one depleted and one regrown stage");
        }

        final GeneratorStage firstStage = generatorStages.get(0);
        if (firstStage.getState() != GrowthState.DEPLETED) {
            throw new InvalidConfigurationException(sequence, "The first stage must be depleted");
        }

        if (firstStage.getGrowthFunction() != null) {
            throw new InvalidConfigurationException(sequence, "The first stage cannot have a growth function");
        }

        if (firstStage.getGrowthTime() == 0) {
            throw new InvalidConfigurationException(sequence, "The depleted stage must have a growth time set");
        }

        final GeneratorStage lastStage = generatorStages.get(generatorStages.size() - 1);
        if (lastStage.getState() != GrowthState.REGROWN) {
            throw new InvalidConfigurationException(sequence, "The last stage must be regrown");
        }

        boolean firstHarvestableFound = false;
        for (int i = 1; i < generatorStages.size(); i++) {
            final GeneratorStage stage = generatorStages.get(i);
            if (stage.getState() == GrowthState.DEPLETED) {
                throw new InvalidConfigurationException(sequence, "Only the first stage should be depleted");
            }

            if (!firstHarvestableFound && stage.getState().isHarvestable()) {
                if (stage.getGrowthChance() != null && stage.getGrowthChance() != 1.0) {
                    throw new InvalidConfigurationException(sequence, "Generator must have at least one unripe/ripe/regrown stage with 100% growth chance");
                }
                firstHarvestableFound = true;
            }
        }

        generator.setItemType(generator.getLastStage().getStructure().getMostCommonMaterial());

        return generator;
    }

    private GeneratorStage loadStage(GeneratorTemplate generator, ConfigSection section) {
        final GrowthState state = section.getRequired("type|state", GrowthState.class);

        final BlockStructure structure = section.contains("structure|blocks") ?
                section.getRequired("structure|blocks", BlockStructure.class) :
                section.loadRequired(BlockStructure.class);

        List<Material> decorativeBlocks = section.getAll("decorative-blocks", Material.class);

        Boolean defaultDrops = section.getBoolean("default-drops|drops").withDefault(null);
        boolean dropItems = defaultDrops != null ? defaultDrops :
                section.getBoolean("drop-items").withDefault(false);
        boolean dropExp = defaultDrops != null ? defaultDrops :
                section.getBoolean("drop-exp|drop-xp").withDefault(false);

        Double health = section.getDouble("health")
                .filter(Predicates.greaterThan(0), "Health must be greater than or equal to 1.")
                .withDefault(null);

        boolean idle = section.getBoolean("idle").withDefault(health != null);

        if (health != null && !idle) {
            throw new InvalidConfigurationException(section, "idle", "Generator Stage with health set must be idle.");
        }

        int growthTime = section.get("growth|growth-time", Ticks.class)
                .map(Ticks::getCount)
                .withDefault(0);
        Double chance = section.getDouble("chance|growth-chance").withDefault(null);

        int clickCooldown = section.getInteger("click-cooldown")
                .filter(Predicates.greaterThan(1), "Click cooldown must be greater than 1")
                .withDefault(plugin.getOrestackOptions().getClickCooldown());

        int hitCooldown = section.getInteger("hit-cooldown")
                .filter(Predicates.greaterThan(1), "Hit cooldown must be greater than 1")
                .withDefault(plugin.getOrestackOptions().getHitCooldown());

        int harvestCooldown = section.getInteger("harvest-cooldown")
                .filter(Predicates.greaterThan(1), "Harvest cooldown must be greater than 1")
                .withDefault(plugin.getOrestackOptions().getClickCooldown());

        Hologram hologram = null;
        if (SpigotServer.isPluginEnabled("DecentHolograms")) {
            hologram = section.get("hologram", Hologram.class).withDefault(null);
        }

        Function onBreak = section.get("on-break", Function.class).withDefault(null);
        Function onGrowth = section.get("on-growth", Function.class).withDefault(null);
        Function onClick = section.get("on-click", Function.class).withDefault(null);
        Function onHit = section.get("on-hit", Function.class).withDefault(null);
        Function onHarvest = section.get("on-harvest", Function.class).withDefault(null);
        Function onDestroy = section.get("on-destroy", Function.class).withDefault(null);

        return new GeneratorStage(generator, state, structure, decorativeBlocks, dropItems, dropExp, idle,
                growthTime, chance, health, clickCooldown, hitCooldown, harvestCooldown, hologram,
                onBreak, onGrowth, onClick, onHit, onHarvest, onDestroy);
    }

}