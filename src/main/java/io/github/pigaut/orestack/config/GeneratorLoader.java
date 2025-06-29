package io.github.pigaut.orestack.config;

import io.github.pigaut.orestack.generator.template.*;
import io.github.pigaut.orestack.stage.*;
import io.github.pigaut.voxel.core.function.*;
import io.github.pigaut.voxel.core.function.interact.block.*;
import io.github.pigaut.voxel.core.structure.*;
import io.github.pigaut.voxel.hologram.*;
import io.github.pigaut.voxel.plugin.manager.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.configurator.loader.*;
import org.bukkit.*;
import org.jetbrains.annotations.*;

import java.util.*;
import java.util.stream.*;

public class GeneratorLoader implements ConfigLoader<GeneratorTemplate> {

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
        final String group = PathGroup.byFile(root.getFile(), "generators", true);
        final List<GeneratorStage> generatorStages = new ArrayList<>();
        final GeneratorTemplate generator = new GeneratorTemplate(name, group, sequence, generatorStages);
        for (ConfigSection nestedSection : sequence.getNestedSections()) {
            generatorStages.add(loadStage(generator, nestedSection));
        }

        if (generatorStages.size() < 2) {
            throw new InvalidConfigurationException(sequence, "Generator must have at least one depleted and one replenished stage");
        }

        final GeneratorStage firstStage = generatorStages.get(0);
        if (firstStage.getState() != GeneratorState.DEPLETED) {
            throw new InvalidConfigurationException(sequence, "The first stage must be depleted");
        }

        if (firstStage.getGrowthFunction() != null) {
            throw new InvalidConfigurationException(sequence, "The first stage cannot have a growth function");
        }

        if (firstStage.getGrowthTime() == 0) {
            throw new InvalidConfigurationException(sequence, "The depleted stage must have a growth time set");
        }

        final GeneratorStage lastStage = generatorStages.get(generatorStages.size() - 1);
        if (lastStage.getState() != GeneratorState.REPLENISHED) {
            throw new InvalidConfigurationException(sequence, "The last stage must be replenished");
        }

        boolean firstHarvestableFound = false;
        for (int i = 1; i < generatorStages.size(); i++) {
            final GeneratorStage stage = generatorStages.get(i);
            if (stage.getState() == GeneratorState.DEPLETED) {
                throw new InvalidConfigurationException(sequence, "Only the first stage should be depleted");
            }

            if (!firstHarvestableFound && stage.getState().isHarvestable()) {
                if (stage.getGrowthChance() != null && stage.getGrowthChance() != 1.0) {
                    throw new InvalidConfigurationException(sequence, "Generator must have at least one harvestable/replenished stage with 100% growth chance");
                }
                firstHarvestableFound = true;
            }
        }

        final List<BlockChange> structure = generator.getLastStage().getStructure().getBlockChanges();
        if (structure.size() == 1) {
            generator.setItemType(structure.get(0).getType());
        }
        else {
            final Material mostCommonBlockType = structure.stream()
                    .collect(Collectors.groupingBy(BlockChange::getType, Collectors.counting()))
                    .entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .map(Map.Entry::getKey)
                    .orElse(Material.TERRACOTTA);
            generator.setItemType(mostCommonBlockType);
        }

        return generator;
    }

    private GeneratorStage loadStage(GeneratorTemplate generator, ConfigSection config) {
        final GeneratorState state = config.get("type|state", GeneratorState.class);
        BlockStructure structure = config.getOptional("structure|structures", BlockStructure.class).orElse(null);
        if (structure == null) {
            structure = config.load(BlockStructure.class);
        }
        final boolean dropItems = config.getOptionalBoolean("drop-item|drop-items").orElse(true);
        final Integer expToDrop = config.getOptionalInteger("exp-to-drop").orElse(null);
        final boolean regrow = config.getOptionalBoolean("regrow").orElse(true);
        final int growthTime = config.getOptionalInteger("growth|growth-time").orElse(0);
        final Double chance = config.getOptionalDouble("chance|growth-chance").orElse(null);
        final Function onBreak = config.getOptional("on-break", Function.class).orElse(null);
        final Function onGrowth = config.getOptional("on-growth", Function.class).orElse(null);
        final BlockClickFunction onClick = config.getOptional("on-click", BlockClickFunction.class).orElse(null);
        final Hologram hologram = config.getOptional("hologram", Hologram.class).orElse(null);

        if (growthTime < 0) {
            throw new InvalidConfigurationException(config, "growth", "The growth timer must be a positive number");
        }

        return new GeneratorStage(generator, state, structure, dropItems, expToDrop, regrow, growthTime,
                chance, onBreak, onGrowth, onClick, hologram);
    }

}