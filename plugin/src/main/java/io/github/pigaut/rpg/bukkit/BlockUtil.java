package io.github.pigaut.rpg.bukkit;

import io.github.pigaut.rpg.core.transform.Rotation;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.block.Sign;
import org.bukkit.block.data.*;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.type.*;
import org.bukkit.block.data.type.Bed;
import org.jetbrains.annotations.*;

import java.util.*;

public class BlockUtil {

    public static final Set<BlockFace> WALL_FACES = Set.of(BlockFace.NORTH,
            BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST);

    @Deprecated
    public static boolean isPlaced(@NotNull Location location, @NotNull Rotation rotation, @NotNull Material type,
                                   @Nullable Integer age, @Nullable BlockFace direction, @Nullable Set<BlockFace> facingDirections,
                                   @Nullable Axis orientation, @Nullable Boolean open, @Nullable Bisected.Half half, @Nullable Stairs.Shape stairShape,
                                   @Nullable Slab.Type slabType, @Nullable Door.Hinge doorHinge, @Nullable Bed.Part bedPart,
                                   @Nullable Bamboo.Leaves bambooLeaves, @Nullable Boolean caveVinesBerries, @Nullable List<String> signLines) {

        BlockData blockData = location.getBlock().getBlockData();
        if (blockData.getMaterial() != type) {
            return false;
        }

        if (age != null && ((Ageable) blockData).getAge() != age) {
            return false;
        }

        if (direction != null) {
            if (blockData instanceof Directional directional) {
                if (directional.getFacing() != rotation.translateBlockFace(direction)) {
                    return false;
                }
            } else if (((Rotatable) blockData).getRotation() != rotation.translateBlockFace(direction)) {
                return false;
            }
        }

        if (facingDirections != null
                && !((MultipleFacing) blockData).getFaces().equals(rotation.translateBlockFaces(facingDirections))) {
            return false;
        }

        if (orientation != null && ((Orientable) blockData).getAxis() != rotation.translateAxis(orientation)) {
            return false;
        }

        if (open != null && open != ((Openable) blockData).isOpen()) {
            return false;
        }

        if (half != null && half != ((Bisected) blockData).getHalf()) {
            return false;
        }

        if (stairShape != null && stairShape != ((Stairs) blockData).getShape()) {
            return false;
        }

        if (slabType != null && slabType != ((Slab) blockData).getType()) {
            return false;
        }

        if (doorHinge != null && doorHinge != ((Door) blockData).getHinge()) {
            return false;
        }

        if (bedPart != null && bedPart != ((org.bukkit.block.data.type.Bed) blockData).getPart()) {
            return false;
        }

        if (bambooLeaves != null && bambooLeaves != ((Bamboo) blockData).getLeaves()) {
            return false;
        }

        if (caveVinesBerries != null && caveVinesBerries != ((CaveVinesPlant) blockData).isBerries()) {
            return false;
        }

        if (signLines != null) {
            Sign sign = (Sign) blockData;
            for (int i = 0; i < 4; i++) {
                if (!sign.getLine(i).equals(signLines.get(i))) {
                    return false;
                }
            }
        }

        return true;
    }

    @Deprecated
    public static void place(@NotNull Location location, @NotNull Rotation rotation, boolean applyPhysics, @NotNull Material type,
                             @Nullable Integer age, @Nullable BlockFace direction, @Nullable Set<BlockFace> facingDirections,
                             @Nullable Axis orientation, @Nullable Boolean open, @Nullable Bisected.Half half, @Nullable Stairs.Shape stairShape,
                             @Nullable Slab.Type slabType, @Nullable Door.Hinge doorHinge, @Nullable Bed.Part bedPart,
                             @Nullable Bamboo.Leaves bambooLeaves, @Nullable Boolean caveVinesBerries, @Nullable List<String> signLines) {

        Block block = location.getBlock();
        BlockData blockData = block.getBlockData();

        if (block.getType() != type) {
            block.setType(type, applyPhysics);
            blockData = block.getBlockData();
        }

        boolean changed = applyChanges(blockData, rotation, age, direction, facingDirections, orientation, open,
                half, stairShape, slabType, doorHinge, bedPart, bambooLeaves, caveVinesBerries, signLines);

        if (changed) {
            block.setBlockData(blockData, applyPhysics);
        }
    }

    @Deprecated
    public static boolean applyChanges(@NotNull BlockData blockData, @NotNull Rotation rotation,
                                    @Nullable Integer age, @Nullable BlockFace direction, @Nullable Set<BlockFace> facingDirections,
                                    @Nullable Axis orientation, @Nullable Boolean open, @Nullable Bisected.Half half, @Nullable Stairs.Shape stairShape,
                                    @Nullable Slab.Type slabType, @Nullable Door.Hinge doorHinge, @Nullable Bed.Part bedPart,
                                    @Nullable Bamboo.Leaves bambooLeaves, @Nullable Boolean caveVinesBerries, @Nullable List<String> signLines) {
        boolean changed = false;

        if (age != null) {
            Ageable ageable = (Ageable) blockData;
            if (ageable.getAge() != age) {
                ageable.setAge(age);
                changed = true;
            }
        }

        if (direction != null) {
            BlockFace translated = rotation.translateBlockFace(direction);
            if (blockData instanceof Directional directional) {
                if (directional.getFacing() != translated) {
                    directional.setFacing(translated);
                    changed = true;
                }
            } else if (blockData instanceof Rotatable rotatable) {
                if (rotatable.getRotation() != translated) {
                    rotatable.setRotation(translated);
                    changed = true;
                }
            }
        }

        if (facingDirections != null) {
            MultipleFacing multipleFacing = (MultipleFacing) blockData;
            for (BlockFace facing : rotation.translateBlockFaces(facingDirections)) {
                if (!multipleFacing.hasFace(facing)) {
                    multipleFacing.setFace(facing, true);
                    changed = true;
                }
            }
        }

        if (orientation != null) {
            Orientable orientable = (Orientable) blockData;
            Axis axis = rotation.translateAxis(orientation);
            if (orientable.getAxis() != axis) {
                orientable.setAxis(axis);
                changed = true;
            }
        }

        if (open != null) {
            Openable openable = (Openable) blockData;
            if (openable.isOpen() != open) {
                openable.setOpen(open);
                changed = true;
            }
        }

        if (half != null) {
            Bisected bisected = (Bisected) blockData;
            if (bisected.getHalf() != half) {
                bisected.setHalf(half);
                changed = true;
            }
        }

        if (stairShape != null) {
            Stairs stairs = (Stairs) blockData;
            if (stairs.getShape() != stairShape) {
                stairs.setShape(stairShape);
                changed = true;
            }
        }

        if (slabType != null) {
            Slab slab = (Slab) blockData;
            if (slab.getType() != slabType) {
                slab.setType(slabType);
                changed = true;
            }
        }

        if (doorHinge != null) {
            Door door = (Door) blockData;
            if (door.getHinge() != doorHinge) {
                door.setHinge(doorHinge);
                changed = true;
            }
        }

        if (bedPart != null) {
            Bed bed = (Bed) blockData;
            if (bed.getPart() != bedPart) {
                bed.setPart(bedPart);
                changed = true;
            }
        }

        if (bambooLeaves != null) {
            Bamboo bamboo = (Bamboo) blockData;
            if (bamboo.getLeaves() != bambooLeaves) {
                bamboo.setLeaves(bambooLeaves);
                changed = true;
            }
        }

        if (caveVinesBerries != null) {
            CaveVinesPlant caveVines = (CaveVinesPlant) blockData;
            if (caveVinesBerries != caveVines.isBerries()) {
                caveVines.setBerries(caveVinesBerries);
                changed = true;
            }
        }

        if (signLines != null) {
            Sign sign = (Sign) blockData;
            for (int i = 0; i < 4; i++) {
                String validLine = signLines.get(i);
                if (!sign.getLine(i).equals(validLine)) {
                    sign.setLine(i, validLine);
                }
            }
        }

        return changed;
    }

}
