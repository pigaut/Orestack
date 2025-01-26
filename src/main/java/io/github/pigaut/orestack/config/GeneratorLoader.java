package io.github.pigaut.orestack.config;

import io.github.pigaut.orestack.generator.*;
import io.github.pigaut.orestack.stage.*;
import io.github.pigaut.voxel.function.*;
import io.github.pigaut.voxel.function.action.*;
import io.github.pigaut.voxel.function.interact.block.*;
import io.github.pigaut.voxel.hologram.*;
import io.github.pigaut.voxel.yaml.*;
import io.github.pigaut.voxel.yaml.configurator.loader.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.block.data.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class GeneratorLoader implements ConfigLoader<Generator> {

    @Override
    public @NotNull String getProblemDescription() {
        return "Could not load generator";
    }

    @Override
    public @NotNull Generator loadFromSequence(@NotNull ConfigSequence config) throws InvalidConfigurationException {
        final String name = config.getRoot().getName();
        final List<GeneratorStage> generatorStages = new ArrayList<>();
        final Generator generator = new Generator(name, generatorStages);
        for (ConfigSection nestedSection : config.getNestedSections()) {
            generatorStages.add(loadStage(generator, nestedSection));
        }

        if (generatorStages.size() < 2) {
            throw new InvalidConfigurationException(config, "Generator must have one depleted and one replenished stage");
        }

        if (generatorStages.get(0).getState() != GeneratorState.DEPLETED) {
            throw new InvalidConfigurationException(config, "The first stage must be depleted");
        }

        if (generatorStages.get(generatorStages.size() - 1).getState() != GeneratorState.REPLENISHED) {
            throw new InvalidConfigurationException(config, "The last stage must be replenished");
        }

        for (int i = 1; i < generatorStages.size(); i++) {
            if (generatorStages.get(i).getState() == GeneratorState.DEPLETED) {
                throw new InvalidConfigurationException(config, "Only the first stage is depleted");
            }
        }
        return generator;
    }

    private GeneratorStage loadStage(Generator generator, ConfigSection config) {
        config.setProblemDescription("Could not load generator stage");

        final GeneratorState state = config.get("type", GeneratorState.class);
        final Material block = config.get("block|resource", Material.class);
        final Integer age = config.getOptionalInteger("age").orElse(null);
        final BlockFace facingDirection = config.getOptional("direction|facing", BlockFace.class).orElse(null);
        final boolean dropItems = config.getOptionalBoolean("drop-items").orElse(true);
        final Integer expToDrop = config.getOptionalInteger("exp-to-drop").orElse(null);
        final int growthTime = config.getOptionalInteger("growth|growth-time").orElse(0);
        final Double chance = config.getOptionalDouble("chance|growth-chance").orElse(null);
        final Function onBreak = config.getOptional("on-break", Function.class).orElse(null);
        final Function onGrowth = config.getOptional("on-growth", Function.class).orElse(null);
        final BlockClickFunction onClick = config.getOptional("on-click", BlockClickFunction.class).orElse(null);
        final Hologram hologram = config.getOptional("hologram", Hologram.class).orElse(null);

        if (!block.isBlock()) {
            throw new InvalidConfigurationException(config, "block", "The material must be a block");
        }

        if (age != null) {
            if (block.createBlockData() instanceof Ageable ageable) {
                final int maximumAge = ageable.getMaximumAge();
                if (age < 0 || age > maximumAge) {
                    throw new InvalidConfigurationException(config, "age", "The age for this block must be a value between 0 and " + maximumAge + " (inclusive)");
                }
            }
            else {
                throw new InvalidConfigurationException(config, "age", "The current block is not ageable, please remove the age parameter");
            }
        }

        if (facingDirection != null) {
            if (block.createBlockData() instanceof Directional directional) {
                if (!directional.getFaces().contains(facingDirection)) {
                    throw new InvalidConfigurationException(config, "direction", "Block cannot face that direction");
                }
            }
            else {
                throw new InvalidConfigurationException(config, "direction", "The current block is not directional, please remove the direction parameter");
            }
        }

        if (growthTime < 0) {
            throw new InvalidConfigurationException(config, "growth", "The growth timer must be a positive number");
        }

        return new GeneratorStage(generator, state, block, age, facingDirection, dropItems, expToDrop, growthTime,
                chance, onBreak, onGrowth, onClick, hologram);
    }

}