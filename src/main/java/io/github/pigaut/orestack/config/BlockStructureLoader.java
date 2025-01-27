package io.github.pigaut.orestack.config;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.structure.*;
import io.github.pigaut.voxel.yaml.*;
import io.github.pigaut.voxel.yaml.configurator.loader.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.block.data.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class BlockStructureLoader implements ConfigLoader<BlockStructure> {

    private final OrestackPlugin plugin;

    public BlockStructureLoader(OrestackPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getProblemDescription() {
        return "Could not load block structure";
    }

    @Override
    public @NotNull BlockStructure loadFromScalar(ConfigScalar scalar) throws InvalidConfigurationException {
        final String structureName = scalar.toString();
        final BlockStructure blockStructure = plugin.getBlockStructure(structureName);
        if (blockStructure == null) {
            throw new InvalidConfigurationException(scalar, "Could not find any block structure with name:" + structureName);
        }
        return blockStructure;
    }

    @Override
    public @NotNull BlockStructure loadFromSection(@NotNull ConfigSection config) throws InvalidConfigurationException {
        final Material material = config.get("block", Material.class);
        final Integer age = config.getOptionalInteger("age").orElse(null);
        final BlockFace direction = config.getOptional("direction|facing", BlockFace.class).orElse(null);

        if (!material.isBlock()) {
            throw new InvalidConfigurationException(config, "block", "The material must be a block");
        }

        if (age != null) {
            if (material.createBlockData() instanceof Ageable ageable) {
                final int maximumAge = ageable.getMaximumAge();
                if (age < 0 || age > maximumAge) {
                    throw new InvalidConfigurationException(config, "age", "The age for this block must be a value between 0 and " + maximumAge + " (inclusive)");
                }
            }
            else {
                throw new InvalidConfigurationException(config, "age", "The current block is not ageable, please remove the age parameter");
            }
        }

        if (direction != null) {
            if (material.createBlockData() instanceof Directional directional) {
                if (!directional.getFaces().contains(direction)) {
                    throw new InvalidConfigurationException(config, "direction", "Block cannot face that direction");
                }
            }
            else {
                throw new InvalidConfigurationException(config, "direction", "The current block is not directional, please remove the direction parameter");
            }
        }

        final int offsetX = config.getOptionalInteger("offset.x").orElse(0);
        final int offsetY = config.getOptionalInteger("offset.y").orElse(0);
        final int offsetZ = config.getOptionalInteger("offset.z").orElse(0);
        if (offsetX != 0 || offsetY != 0 || offsetZ != 0) {
            return new OffsetBlockStructure(material, age, direction, offsetX, offsetY, offsetZ);
        }
        return new SingleBlockStructure(material, age, direction);
    }

    @Override
    public @NotNull BlockStructure loadFromSequence(@NotNull ConfigSequence sequence) throws InvalidConfigurationException {
        final List<BlockStructure> structures = sequence.getAll(BlockStructure.class);
        if (structures.size() < 2) {
            throw new InvalidConfigurationException(sequence, "Structure must have at least two blocks in it");
        }
        return new MultiBlockStructure(structures);
    }


}
