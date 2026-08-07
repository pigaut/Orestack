package io.github.pigaut.rpg.module.structure.block.specific;

import io.github.pigaut.rpg.core.transform.Rotation;
import io.github.pigaut.rpg.module.structure.block.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.block.data.type.*;
import org.jetbrains.annotations.*;

public class PistonBlockTemplate extends AbstractBlockTemplate<Piston> {

    private final BlockFace facing;
    private final boolean extended;

    public PistonBlockTemplate(@NotNull Material type, @NotNull BlockFace facing, boolean extended) {
        super(type, Piston.class);
        this.facing = facing;
        this.extended = extended;
    }

    @Override
    public boolean matchBlockData(@NotNull Piston blockData, @NotNull Rotation rotation) {
        return blockData.getFacing() == rotation.translateBlockFace(facing) &&
                blockData.isExtended() == extended;
    }

    @Override
    public void updateBlockData(@NotNull Piston blockData, @NotNull Rotation rotation) {
        blockData.setFacing(rotation.translateBlockFace(facing));
        blockData.setExtended(extended);
    }
}
