package io.github.pigaut.orestack.config;

import io.github.pigaut.orestack.generator.template.*;
import io.github.pigaut.orestack.stage.*;
import io.github.pigaut.voxel.core.function.*;
import io.github.pigaut.voxel.core.function.interact.block.*;
import io.github.pigaut.voxel.core.structure.*;
import io.github.pigaut.voxel.hologram.*;
import io.github.pigaut.voxel.plugin.manager.*;
import io.github.pigaut.voxel.server.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.configurator.load.*;
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
        final String group = Group.byFile(root.getFile(), "generators", true);
        final List<GeneratorStage> generatorStages = new ArrayList<>();
        final GeneratorTemplate generator = new GeneratorTemplate(name, group, generatorStages);
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

    private GeneratorStage loadStage(GeneratorTemplate generator, ConfigSection section) {
        final GeneratorState state = section.getRequired("type|state", GeneratorState.class);
        final BlockStructure structure = section.contains("structure|blocks") ?
                section.getRequired("structure|blocks", BlockStructure.class) :
                section.loadRequired(BlockStructure.class);
        final boolean dropItems = section.getBoolean("drop-item|drop-items").throwOrElse(true);
        final Integer expToDrop = section.getInteger("exp-to-drop").throwOrElse(null);
        final boolean regrow = section.getBoolean("regrow").throwOrElse(true);
        final int growthTime = section.getInteger("growth|growth-time").throwOrElse(0);
        final Double chance = section.getDouble("chance|growth-chance").throwOrElse(null);
        final Function onBreak = section.get("on-break", Function.class).throwOrElse(null);
        final Function onGrowth = section.get("on-growth", Function.class).throwOrElse(null);
        final BlockClickFunction onClick = section.get("on-click", BlockClickFunction.class).throwOrElse(null);

        Hologram hologram = null;
        if (SpigotServer.isPluginEnabled("DecentHolograms")) {
            hologram = section.get("hologram", Hologram.class).throwOrElse(null);
        }

        if (growthTime < 0) {
            throw new InvalidConfigurationException(section, "growth", "The growth timer must be a positive number");
        }

        return new GeneratorStage(generator, state, structure, dropItems, expToDrop, regrow, growthTime,
                chance, onBreak, onGrowth, onClick, hologram);
    }

}