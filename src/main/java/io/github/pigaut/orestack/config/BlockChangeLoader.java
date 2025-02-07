package io.github.pigaut.orestack.config;

import io.github.pigaut.orestack.structure.*;
import io.github.pigaut.voxel.yaml.*;
import io.github.pigaut.voxel.yaml.configurator.loader.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.block.data.*;
import org.bukkit.block.data.type.*;
import org.bukkit.block.data.type.Bed;
import org.jetbrains.annotations.*;

public class BlockChangeLoader implements ConfigLoader<BlockChange> {

    @Override
    public @Nullable String getProblemDescription() {
        return "invalid block";
    }

    @Override
    public @NotNull BlockChange loadFromSection(@NotNull ConfigSection config) throws InvalidConfigurationException {
        final Material material = config.get("block", Material.class);
        final Integer age = config.getOptionalInteger("age").orElse(null);
        final BlockFace direction = config.getOptional("direction|facing", BlockFace.class).orElse(null);
        final Axis orientation = config.getOptional("orientation", Axis.class).orElse(null);
        final Boolean open = config.getOptionalBoolean("open").orElse(null);
        final Bisected.Half half = config.getOptional("half", Bisected.Half.class).orElse(null);
        final Stairs.Shape stairShape = config.getOptional("stair-shape|stairs-shape|stairs", Stairs.Shape.class).orElse(null);
        final Door.Hinge doorHinge = config.getOptional("door-hinge|door", Door.Hinge.class).orElse(null);
        final Bed.Part bedPart = config.getOptional("bed-part|bed", Bed.Part.class).orElse(null);

        if (!material.isBlock()) {
            throw new InvalidConfigurationException(config, "block", "The material must be a block");
        }

        final BlockData blockData = material.createBlockData();

        if (age != null) {
            if (blockData instanceof Ageable ageable) {
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
            if (blockData instanceof Directional directional) {
                if (!directional.getFaces().contains(direction)) {
                    throw new InvalidConfigurationException(config, "direction", "Block cannot face that direction");
                }
            }
            else if (!(blockData instanceof Rotatable)) {
                throw new InvalidConfigurationException(config, "direction", "The current block is not directional, please remove the direction parameter");
            }
        }

        if (orientation != null) {
            if (blockData instanceof Orientable orientable) {
                if (!orientable.getAxes().contains(orientation)) {
                    throw new InvalidConfigurationException(config, "orientation", "Block cannot be oriented to that axis");
                }
            } else {
                throw new InvalidConfigurationException(config, "orientation", "The current block is not orientable, please remove the orientation parameter");
            }
        }

        if (open != null && !(blockData instanceof Openable)) {
            throw new InvalidConfigurationException(config, "open", "The current block is not openable, please remove the open parameter");
        }

        if (half != null && !(blockData instanceof Bisected)) {
            throw new InvalidConfigurationException(config, "half", "The current block is not bisected, please remove the half parameter");
        }

        if (stairShape != null && !(blockData instanceof Stairs)) {
            throw new InvalidConfigurationException(config, "stairs-shape", "The current block is not a stairs, please remove the stairs-shape parameter");
        }

        if (doorHinge != null && !(blockData instanceof Door)) {
            throw new InvalidConfigurationException(config, "door-hinge", "The current block is not a door, please remove the door-hinge parameter");
        }

        final int offsetX = config.getOptionalInteger("offset.x").orElse(0);
        final int offsetY = config.getOptionalInteger("offset.y").orElse(0);
        final int offsetZ = config.getOptionalInteger("offset.z").orElse(0);

        return new BlockChange(material, age, direction, orientation,
                open, half, stairShape, doorHinge, bedPart, offsetX, offsetY, offsetZ);
    }

}
