package io.github.pigaut.orestack.structure;

import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.block.data.*;
import org.jetbrains.annotations.*;
import io.github.pigaut.voxel.util.Rotation;

import java.util.*;

public class OriginBlockStructure implements BlockStructure {

    private final Material type;
    private final @Nullable Integer age;
    private final @Nullable BlockFace direction;
    private final @Nullable Axis orientation;

    public OriginBlockStructure(Material type, @Nullable Integer age, @Nullable BlockFace direction, @Nullable Axis orientation) {
        this.type = type;
        this.age = age;
        this.direction = direction;
        this.orientation = orientation;
    }

    public Material getType() {
        return type;
    }

    public @Nullable Integer getAge() {
        return age;
    }

    public @Nullable BlockFace getDirection() {
        return direction;
    }

    @Override
    public List<Block> getBlocks(Location origin, Rotation rotation) {
        return List.of(origin.getBlock());
    }

    @Override
    public void removeBlocks(Location origin, Rotation rotation) {
        origin.getBlock().setType(Material.AIR);
    }

    @Override
    public boolean matchBlocks(Location location, Rotation rotation) {
        final BlockData blockData = location.getBlock().getBlockData();
        return blockData.getMaterial() == type
                && (age == null || ((Ageable) blockData).getAge() == age)
                && (direction == null || ((Directional) blockData).getFacing() == rotation.translateBlockFace(direction))
                && (orientation == null || ((Orientable) blockData).getAxis() == rotation.translateAxis(orientation));
    }

    @Override
    public void createBlocks(Location origin, Rotation rotation) {
        final Block block = origin.getBlock();
        if (block.getType() != type) {
            block.setType(type);
        }
        if (age != null) {
            final Ageable ageable = (Ageable) block.getBlockData();
            ageable.setAge(age);
            block.setBlockData(ageable);
        }
        if (direction != null) {
            final Directional directional = (Directional) block.getBlockData();
            directional.setFacing(rotation.translateBlockFace(direction));
            block.setBlockData(directional);
        }
        if (orientation != null) {
            final Orientable orientable = (Orientable) block.getBlockData();
            orientable.setAxis(rotation.translateAxis(orientation));
            block.setBlockData(orientable);
        }
    }

}
