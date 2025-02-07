package io.github.pigaut.orestack.structure;

import io.github.pigaut.voxel.util.Rotation;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.block.data.*;
import org.bukkit.block.data.type.*;
import org.jetbrains.annotations.*;

public class BlockChange {

    private final Material type;
    private final @Nullable Integer age;
    private final @Nullable BlockFace direction;
    private final @Nullable Axis orientation;
    private final @Nullable Boolean open;
    private final @Nullable Boolean tall;
    private final @Nullable Stairs.Shape stairShape;
    private final @Nullable Door.Hinge doorHinge;
    private final int offsetX;
    private final int offsetY;
    private final int offsetZ;

    public BlockChange(Material type, @Nullable Integer age, @Nullable BlockFace direction, @Nullable Axis orientation,
                       @Nullable Boolean open, @Nullable Boolean tall, @Nullable Stairs.Shape stairShape,
                       @Nullable Door.Hinge doorHinge, int offsetX, int offsetY, int offsetZ) {
        this.type = type;
        this.age = age;
        this.direction = direction;
        this.orientation = orientation;
        this.open = open;
        this.tall = tall;
        this.stairShape = stairShape;
        this.doorHinge = doorHinge;
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

        if (direction != null) {
            if (blockData instanceof Directional directional
                    && directional.getFacing() != rotation.translateBlockFace(direction)) {
                return false;
            }
            if (((Rotatable) blockData).getRotation() != rotation.translateBlockFace(direction)) {
                return false;
            }
        }

        if (orientation != null && ((Orientable) blockData).getAxis() != rotation.translateAxis(orientation)) {
            return false;
        }

        if (open != null && (open != ((Openable) blockData).isOpen())) {
            return false;
        }

        if (stairShape != null && stairShape != ((Stairs) blockData).getShape()) {
            return false;
        }

        if (doorHinge != null && doorHinge != ((Door) blockData).getHinge()) {
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
        BlockData blockData = block.getBlockData();

        if (block.getType() != type) {
            block.setType(type);
            blockData = block.getBlockData();
        }

        if (age != null) {
            ((Ageable) blockData).setAge(age);
        }
        if (direction != null) {
            if (blockData instanceof Directional) {
                ((Directional) blockData).setFacing(rotation.translateBlockFace(direction));
            } else {
                ((Rotatable) blockData).setRotation(rotation.translateBlockFace(direction));
            }
        }
        if (orientation != null) {
            ((Orientable) blockData).setAxis(rotation.translateAxis(orientation));
        }
        if (open != null) {
            ((Openable) blockData).setOpen(open);
        }
        if (tall != null && tall) {
            ((Bisected) blockData).setHalf(Bisected.Half.BOTTOM);
            block.setBlockData(blockData, false);

            final Block blockAbove = block.getRelative(BlockFace.UP);
            if (blockAbove.getType() != type) {
                block.setType(type);
            }
            block.setType(type);
            System.out.println("TYPE: " + type);
            final Bisected topBlockData = (Bisected) blockAbove.getBlockData();
            topBlockData.setHalf(Bisected.Half.TOP);
            blockAbove.setBlockData(topBlockData, false);
        }
        if (stairShape != null) {
            ((Stairs) blockData).setShape(stairShape);
        }
        if (doorHinge != null) {
            ((Door) blockData).setHinge(doorHinge);
        }

        block.setBlockData(blockData, false);
    }

}
