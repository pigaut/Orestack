package io.github.pigaut.orestack.config;

import io.github.pigaut.orestack.generator.*;
import io.github.pigaut.orestack.stage.*;
import io.github.pigaut.voxel.function.*;
import io.github.pigaut.voxel.function.action.*;
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
            throw new InvalidConfigurationException(config, "Generator must have at least one gathering stage and an exhausted stage");
        }

        if (generatorStages.get(0).getState() != GeneratorState.DEPLETED) {
            throw new InvalidConfigurationException(config, "First stage must be depleted");
        }

        for (int i = 1; i < generatorStages.size(); i++) {
            if (generatorStages.get(i).getState() == GeneratorState.DEPLETED) {
                throw new InvalidConfigurationException(config, "Only the first stage must be exhausted");
            }
        }

        return new Generator(name, generatorStages);
    }

    // is ageable
    // has_gravity
    // is flamable or is burnable

    private GeneratorStage loadStage(Generator generator, ConfigSection config) {
        config.setProblemDescription("Could not load generator stage");
        final GeneratorState state = config.get("type", GeneratorState.class);
        final Material block = config.get("block|resource", Material.class);
        final Integer age = config.getOptionalInteger("age").orElse(null);
        final BlockFace facingDirection = config.getOptional("direction|facing", BlockFace.class).orElse(null);
        final boolean dropItems = config.getOptionalBoolean("drop-items").orElse(true);
        final int regeneration = config.getOptionalInteger("growth|growth-time").orElse(0);
        final Double chance = config.getOptionalDouble("chance|growth-chance").orElse(null);
        final List<Function> onBreak = config.getAll("on-break", Function.class);
        final List<Function> onGrowth = config.getAll("on-growth", Function.class);
        final List<BlockClickFunction> onClick = config.getAll("on-click", BlockClickFunction.class);

        if (!block.isBlock()) {
            throw new InvalidConfigurationException(config, "resource", "Resource must be a block");
        }

        if (age != null) {
            if (block.createBlockData() instanceof Ageable ageable) {
                final int maximumAge = ageable.getMaximumAge();
                if (age < 0 || age > maximumAge) {
                    throw new InvalidConfigurationException(config, "age", "Age must be a value between 0 and " + maximumAge);
                }
            }
            else {
                throw new InvalidConfigurationException(config, "age", "Block is not ageable");
            }
        }

        if (facingDirection != null) {
            if (block.createBlockData() instanceof Directional directional) {
                if (!directional.getFaces().contains(facingDirection)) {
                    throw new InvalidConfigurationException(config, "direction", "Block cannot face that direction");
                }
            }
            else {
                throw new InvalidConfigurationException(config, "direction", "Block is not directional");
            }
        }

        if (regeneration < 0) {
            throw new InvalidConfigurationException(config, "regeneration", "Regeneration timer must be a positive number");
        }

        return new GeneratorStage(generator, state, block, age, facingDirection, dropItems, regeneration, chance, onBreak, onGrowth, onClick);
    }

}