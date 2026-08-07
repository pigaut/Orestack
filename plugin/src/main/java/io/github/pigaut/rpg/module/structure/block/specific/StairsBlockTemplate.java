package io.github.pigaut.rpg.module.structure.block.specific;

import io.github.pigaut.rpg.core.transform.Rotation;
import io.github.pigaut.rpg.module.structure.block.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.block.data.*;
import org.bukkit.block.data.type.*;
import org.jetbrains.annotations.*;

public class StairsBlockTemplate extends AbstractBlockTemplate<Stairs> {

    private final BlockFace direction;
    private final Bisected.Half half;
    private final Stairs.Shape shape;

    public StairsBlockTemplate(@NotNull Material type, @NotNull BlockFace direction,
                               @NotNull Bisected.Half half, Stairs.Shape shape) {
        super(type, Stairs.class);
        this.direction = direction;
        this.half = half;
        this.shape = shape;
    }

    @Override
    public boolean matchBlockData(@NotNull Stairs blockData, @NotNull Rotation rotation) {
        return blockData.getFacing() == rotation.translateBlockFace(direction) &&
                blockData.getHalf() == half &&
                blockData.getShape() == shape;
    }

    @Override
    public void updateBlockData(@NotNull Stairs blockData, @NotNull Rotation rotation) {
        blockData.setFacing(rotation.translateBlockFace(direction));
        blockData.setHalf(half);
        blockData.setShape(shape);
    }

}
