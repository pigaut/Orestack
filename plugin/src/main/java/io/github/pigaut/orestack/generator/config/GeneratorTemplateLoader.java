package io.github.pigaut.orestack.generator.config;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.generator.phase.*;
import io.github.pigaut.orestack.generator.template.*;
import io.github.pigaut.voxel.plugin.manager.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.configurator.load.*;
import org.bukkit.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class GeneratorTemplateLoader implements ConfigLoader<GeneratorTemplate> {

    private final OrestackPlugin plugin;

    public GeneratorTemplateLoader(OrestackPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getErrorDescription() {
        return "invalid generator";
    }

    @Override
    public @NotNull GeneratorTemplate loadFromScalar(ConfigScalar scalar) throws InvalidConfigException {
        String generatorName = scalar.toString();
        GeneratorTemplate generatorTemplate = plugin.getGeneratorTemplate(generatorName);
        if (generatorTemplate == null) {
            throw new InvalidConfigException(scalar, "Could not find generator with name: '" + generatorName + "'");
        }
        return generatorTemplate;
    }

    @Override
    public @NotNull GeneratorTemplate loadFromSequence(@NotNull ConfigSequence sequence) throws InvalidConfigException {
        if (!(sequence instanceof ConfigRoot root) || !root.hasFile()) {
            throw new IllegalArgumentException("Generator can only be loaded from a file config sequence");
        }

        String name = root.getName();
        String group = Group.byFile(root.getFile(), "generators", true);

        List<GeneratorPhase> generatorPhases = sequence.getAllRequired(GeneratorPhase.class);

        if (generatorPhases.size() < 2) {
            throw new InvalidConfigException(sequence, "Generator must have at least one depleted and one regrown phase");
        }

        GeneratorPhase firstPhase = generatorPhases.get(0);
        if (firstPhase.getState() != GrowthState.DEPLETED) {
            throw new InvalidConfigException(sequence, "The first phase must be depleted");
        }

        if (firstPhase.getGrowthFunction() != null) {
            throw new InvalidConfigException(sequence, "The first phase cannot have a growth function");
        }

        if (firstPhase.getGrowthTimeInTicks() == 0) {
            throw new InvalidConfigException(sequence, "The depleted phase must have a growth time set");
        }

        GeneratorPhase lastPhase = generatorPhases.get(generatorPhases.size() - 1);
        if (lastPhase.getState() != GrowthState.REGROWN) {
            throw new InvalidConfigException(sequence, "The last phase must be regrown");
        }

        boolean firstHarvestableFound = false;
        for (int i = 1; i < generatorPhases.size(); i++) {
            final GeneratorPhase phase = generatorPhases.get(i);
            if (phase.getState() == GrowthState.DEPLETED) {
                throw new InvalidConfigException(sequence, "Only the first phase should be depleted");
            }

            if (!firstHarvestableFound && phase.getState().isHarvestable()) {
                if (phase.getGrowthChance() != null && phase.getGrowthChance() != 1.0) {
                    throw new InvalidConfigException(sequence, "Generator must have at least one unripe/ripe/regrown phase with 100% growth chance");
                }
                firstHarvestableFound = true;
            }
        }

        Material generatorItem = lastPhase.getStructureTemplate().getMostCommonMaterial();
        boolean multiBlock = false;
        Double maxHealth = null;
        int totalGrowthTime = 0;

        for (GeneratorPhase phase : generatorPhases) {
            if (phase.getStructureTemplate().hasMultipleBlocks()) {
                multiBlock = true;
            }
            Double phaseHealth = phase.getMaxHealth();
            if (phaseHealth != null) {
                maxHealth = maxHealth != null ? maxHealth + phaseHealth : phaseHealth;
            }
            totalGrowthTime += phase.getGrowthTimeInTicks();
        }

        return new GeneratorTemplate(name, group, generatorPhases, generatorItem, multiBlock, maxHealth, totalGrowthTime);
    }

}