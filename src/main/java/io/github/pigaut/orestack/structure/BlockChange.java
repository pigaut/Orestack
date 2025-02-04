package io.github.pigaut.orestack.structure;

import io.github.pigaut.voxel.util.Rotation;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.block.data.*;
import org.jetbrains.annotations.*;

public class BlockChange {

    private final Material type;
    private final @Nullable Integer age;
    private final @Nullable BlockFace direction;
    private final @Nullable Axis orientation;
    private final int offsetX;
    private final int offsetY;
    private final int offsetZ;

    public BlockChange(Material type, @Nullable Integer age, @Nullable BlockFace direction, @Nullable Axis orientation, int offsetX, int offsetY, int offsetZ) {
        this.type = type;
        this.age = age;
        this.direction = direction;
        this.orientation = orientation;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.offsetZ = offsetZ;
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

    public @NotNull Location getLocation(Location origin, Rotation rotation) {
        return rotation.apply(origin.clone(), offsetX, offsetY, offsetZ);
    }

    public boolean matchBlock(Location origin, Rotation rotation) {
        final BlockData blockData = getLocation(origin, rotation).getBlock().getBlockData();
        if (blockData.getMaterial() != type) {
            return false;
        }
        if (age != null && ((Ageable) blockData).getAge() != age) {
            return false;
        }
        if (direction != null && ((Directional) blockData).getFacing() != rotation.translateBlockFace(direction)) {
            return false;
        }
        if (orientation != null && ((Orientable) blockData).getAxis() != rotation.translateAxis(orientation)) {
            return false;
        }
        return true;
    }

    public Block getBlock(Location origin, Rotation rotation) {
        return getLocation(origin, rotation).getBlock();
    }

    public void removeBlock(Location origin, Rotation rotation) {
        getLocation(origin, rotation).getBlock().setType(Material.AIR);
    }

    public void updateBlock(Location origin, Rotation rotation) {
        final Block block = getLocation(origin, rotation).getBlock();
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
