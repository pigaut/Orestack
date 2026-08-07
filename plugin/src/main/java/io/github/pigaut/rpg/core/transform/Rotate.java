package io.github.pigaut.rpg.core.transform;

import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.bukkit.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.jetbrains.annotations.*;

import java.util.*;
import java.util.stream.*;

import static org.bukkit.block.BlockFace.*;
import static org.bukkit.Axis.*;

public enum Rotate {

    NONE {
        @Override
        public @NotNull Location rotate(@NotNull Location pivot, @NotNull Offset offset) {
            return pivot.clone().add(offset.x, offset.y, offset.z);
        }

        @Override
        public @NotNull Location roll(@NotNull Location pivot, @NotNull Offset offset) {
            return pivot.clone().add(offset.y, -offset.x, offset.z);
        }

        @Override
        public @NotNull Location flip(@NotNull Location pivot, @NotNull Offset offset) {
            return pivot.clone().add(-offset.x, -offset.y, offset.z);
        }

        @Override
        public @NotNull BlockFace rotate(@NotNull BlockFace facing) {
            return facing;
        }

        @Override
        public @NotNull BlockFace roll(@NotNull BlockFace facing) {
            return switch (facing) {
                case NORTH -> DOWN;
                case DOWN -> SOUTH;
                case SOUTH -> UP;
                case UP -> NORTH;
                default -> facing;
            };
        }

        @Override
        public @NotNull BlockFace flip(@NotNull BlockFace facing) {
            if (facing == EAST || facing == WEST) {
                return facing;
            }
            return facing.getOppositeFace();
        }

        @Override
        public @NotNull Axis rotate(@NotNull Axis axis) {
            return axis;
        }

        @Override
        public @NotNull Axis roll(@NotNull Axis axis) {
            return axis == X ? X : axis == Z ? Y : Z;
        }
    },
    RIGHT {
        @Override
        public @NotNull Location rotate(@NotNull Location pivot, @NotNull Offset offset) {
            return pivot.clone().add(-offset.z, offset.y, offset.x);
        }

        @Override
        public @NotNull Location roll(@NotNull Location pivot, @NotNull Offset offset) {
            return pivot.clone().add(offset.y, -offset.z, -offset.x);
        }

        @Override
        public @NotNull Location flip(@NotNull Location pivot, @NotNull Offset offset) {
            return pivot.clone().add(-offset.z, -offset.y, -offset.x);
        }

        @Override
        public @NotNull BlockFace rotate(@NotNull BlockFace facing) {
            return switch (facing) {
                case NORTH -> WEST;
                case WEST -> SOUTH;
                case SOUTH -> EAST;
                case EAST -> NORTH;
                default -> facing;
            };
        }

        @Override
        public @NotNull BlockFace roll(@NotNull BlockFace facing) {
            return switch (facing) {
                case WEST -> DOWN;
                case DOWN -> EAST;
                case EAST -> UP;
                case UP -> WEST;
                default -> facing;
            };
        }

        @Override
        public @NotNull BlockFace flip(@NotNull BlockFace facing) {
            if (facing == NORTH || facing == SOUTH) {
                return facing;
            }
            return facing.getOppositeFace();
        }

        @Override
        public @NotNull Axis rotate(@NotNull Axis axis) {
            return axis == Y ? Y : axis == X ? Z : X;
        }

        @Override
        public @NotNull Axis roll(@NotNull Axis axis) {
            return axis == Z ? Z : axis == X ? Y : X;
        }
    },
    BACK {
        @Override
        public @NotNull Location rotate(@NotNull Location pivot, @NotNull Offset offset) {
            return pivot.clone().add(-offset.x, offset.y, -offset.z);
        }

        @Override
        public @NotNull Location roll(@NotNull Location pivot, @NotNull Offset offset) {
            return pivot.clone().add(-offset.x, offset.z, offset.y);
        }

        @Override
        public @NotNull Location flip(@NotNull Location pivot, @NotNull Offset offset) {
            return pivot.clone().add(-offset.x, -offset.y, offset.z);
        }

        @Override
        public @NotNull BlockFace rotate(@NotNull BlockFace facing) {
            if (facing == UP || facing == DOWN) {
                return facing;
            }
            return facing.getOppositeFace();
        }

        @Override
        public @NotNull BlockFace roll(@NotNull BlockFace facing) {
            return switch (facing) {
                case NORTH -> UP;
                case UP -> SOUTH;
                case SOUTH -> DOWN;
                case DOWN -> NORTH;
                default -> facing;
            };
        }

        @Override
        public @NotNull BlockFace flip(@NotNull BlockFace facing) {
            if (facing == NORTH || facing == SOUTH) {
                return facing;
            }
            return facing.getOppositeFace();
        }

        @Override
        public @NotNull Axis rotate(@NotNull Axis axis) {
            return axis;
        }

        @Override
        public @NotNull Axis roll(@NotNull Axis axis) {
            return axis == X ? X : axis == Z ? Y : Z;
        }
    },
    LEFT {
        @Override
        public @NotNull Location rotate(@NotNull Location pivot, @NotNull Offset offset) {
            return pivot.clone().add(offset.z, offset.y, -offset.x);
        }

        @Override
        public @NotNull Location roll(@NotNull Location pivot, @NotNull Offset offset) {
            return pivot.clone().add(-offset.y, -offset.z, -offset.x);
        }

        @Override
        public @NotNull Location flip(@NotNull Location pivot, @NotNull Offset offset) {
            return pivot.clone().add(offset.z, -offset.y, offset.x);
        }

        @Override
        public @NotNull BlockFace rotate(@NotNull BlockFace facing) {
            return switch (facing) {
                case NORTH -> EAST;
                case EAST -> SOUTH;
                case SOUTH -> WEST;
                case WEST -> NORTH;
                default -> facing;
            };
        }

        @Override
        public @NotNull BlockFace roll(@NotNull BlockFace facing) {
            return switch (facing) {
                case EAST -> DOWN;
                case DOWN -> WEST;
                case WEST -> UP;
                case UP -> EAST;
                default -> facing;
            };
        }

        @Override
        public @NotNull BlockFace flip(@NotNull BlockFace facing) {
            if (facing == EAST || facing == WEST) {
                return facing;
            }
            return facing.getOppositeFace();
        }

        @Override
        public @NotNull Axis rotate(@NotNull Axis axis) {
            return axis == Y ? Y : axis == X ? Z : X;
        }

        @Override
        public @NotNull Axis roll(@NotNull Axis axis) {
            return axis == Z ? Z : axis == X ? Y : X;
        }
    };

    public abstract @NotNull Location rotate(@NotNull Location pivot, @NotNull Offset offset);
    public abstract @NotNull Location roll(@NotNull Location pivot, @NotNull Offset offset);
    public abstract @NotNull Location flip(@NotNull Location pivot, @NotNull Offset offset);

    public abstract @NotNull BlockFace rotate(@NotNull BlockFace facing);
    public abstract @NotNull BlockFace roll(@NotNull BlockFace facing);
    public abstract @NotNull BlockFace flip(@NotNull BlockFace facing);

    public abstract @NotNull Axis rotate(@NotNull Axis axis);
    public abstract @NotNull Axis roll(@NotNull Axis axis);

    public @NotNull Set<BlockFace> rotateAll(@NotNull Collection<@NotNull BlockFace> blockFaces) {
        return blockFaces.stream()
                .map(this::rotate)
                .collect(Collectors.toSet());
    }

    public @NotNull Set<BlockFace> rollAll(@NotNull Collection<@NotNull BlockFace> blockFaces) {
        return blockFaces.stream()
                .map(this::roll)
                .collect(Collectors.toSet());
    }

    public @NotNull Set<BlockFace> flipAll(@NotNull Collection<@NotNull BlockFace> blockFaces) {
        return blockFaces.stream()
                .map(this::flip)
                .collect(Collectors.toSet());
    }

}
