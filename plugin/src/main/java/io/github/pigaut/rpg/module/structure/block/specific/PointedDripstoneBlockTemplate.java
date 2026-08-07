package io.github.pigaut.rpg.module.structure.block.specific;

import io.github.pigaut.rpg.core.transform.Rotation;
import io.github.pigaut.rpg.module.structure.block.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.block.data.type.*;
import org.jetbrains.annotations.*;

public class PointedDripstoneBlockTemplate extends AbstractBlockTemplate<PointedDripstone> {

    private final BlockFace verticalFacing;
    private final PointedDripstone.Thickness thickness;

    public PointedDripstoneBlockTemplate(@NotNull Material type, @NotNull BlockFace verticalFacing,
                                         @NotNull PointedDripstone.Thickness thickness) {
        super(type, PointedDripstone.class);
        this.verticalFacing = verticalFacing;
        this.thickness = thickness;
    }

    @Override
    public boolean matchBlockData(@NotNull PointedDripstone blockData, @NotNull Rotation rotation) {
        return blockData.getVerticalDirection() == verticalFacing &&
                blockData.getThickness() == thickness;
    }

    @Override
    public void updateBlockData(@NotNull PointedDripstone blockData, @NotNull Rotation rotation) {
        blockData.setVerticalDirection(verticalFacing);
        blockData.setThickness(thickness);
    }
}
