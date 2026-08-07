package io.github.pigaut.rpg.core.transform;

import org.bukkit.*;
import org.bukkit.block.*;
import org.jetbrains.annotations.*;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.*;

public enum Rotation {

    NONE {
        @Override
        public Location apply(Location location, double x, double y, double z) {
            return location.add(x, y, z);
        }

        @Override
        public BlockFace translateBlockFace(@NotNull BlockFace blockFace) {
            return blockFace;
        }

        @Override
        public Axis translateAxis(Axis axis) {
            return axis;
        }
    },
    RIGHT {
        @Override
        public Location apply(Location location, double x, double y, double z) {
            return location.add(-z, y, x);
        }

        @Override
        public BlockFace translateBlockFace(@NotNull BlockFace blockFace) {
            return getFaceToRight(blockFace);
        }

        @Override
        public Axis translateAxis(Axis axis) {
            if (axis == Axis.Y) {
                return Axis.Y;
            }
            return axis == Axis.X ? Axis.Z : Axis.X;
        }
    },
    BACK {
        @Override
        public Location apply(Location location, double x, double y, double z) {
            return location.add(-x, y, -z);
        }

        @Override
        public BlockFace translateBlockFace(@NotNull BlockFace blockFace) {
            if (blockFace == BlockFace.UP || blockFace == BlockFace.DOWN) {
                return blockFace;
            }
            return blockFace.getOppositeFace();
        }

        @Override
        public Axis translateAxis(Axis axis) {
            return axis;
        }
    },
    LEFT {
        @Override
        public Location apply(Location location, double x, double y, double z) {
            return location.add(z, y, -x);
        }

        @Override
        public BlockFace translateBlockFace(@NotNull BlockFace blockFace) {
            return getFaceToLeft(blockFace);
        }

        @Override
        public Axis translateAxis(Axis axis) {
            if (axis == Axis.Y) {
                return Axis.Y;
            }
            return axis == Axis.X ? Axis.Z : Axis.X;
        }
    };

    public abstract Location apply(Location location, double x, double y, double z);

    public abstract BlockFace translateBlockFace(@NotNull BlockFace blockFace);

    public abstract Axis translateAxis(Axis axis);

    public Set<BlockFace> translateBlockFaces(@NotNull Collection<@NotNull BlockFace> blockFaces) {
        return blockFaces.stream()
                .map(this::translateBlockFace)
                .collect(Collectors.toSet());
    }

    public static BlockFace getFaceToRight(@NotNull BlockFace face) {
        switch (face) {
            case NORTH:
                return BlockFace.EAST;
            case EAST:
                return BlockFace.SOUTH;
            case SOUTH:
                return BlockFace.WEST;
            case WEST:
                return BlockFace.NORTH;
            case NORTH_EAST:
                return BlockFace.SOUTH_EAST;
            case SOUTH_EAST:
                return BlockFace.SOUTH_WEST;
            case SOUTH_WEST:
                return BlockFace.NORTH_WEST;
            case NORTH_WEST:
                return BlockFace.NORTH_EAST;
            default:
                return face;
        }
    }

    public static BlockFace getFaceToLeft(@NotNull BlockFace face) {
        switch (face) {
            case NORTH:
                return BlockFace.WEST;
            case WEST:
                return BlockFace.SOUTH;
            case SOUTH:
                return BlockFace.EAST;
            case EAST:
                return BlockFace.NORTH;
            case NORTH_EAST:
                return BlockFace.NORTH_WEST;
            case NORTH_WEST:
                return BlockFace.SOUTH_WEST;
            case SOUTH_WEST:
                return BlockFace.SOUTH_EAST;
            case SOUTH_EAST:
                return BlockFace.NORTH_EAST;
            default:
                return face;
        }
    }

    public static Rotation random() {
        Rotation[] values = values();
        return values[ThreadLocalRandom.current().nextInt(values.length)];
    }

}
