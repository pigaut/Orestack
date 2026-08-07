package io.github.pigaut.rpg.module.structure.block.specific;

import io.github.pigaut.rpg.core.transform.Rotation;
import io.github.pigaut.rpg.module.structure.block.*;
import io.github.pigaut.rpg.module.structure.block.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.block.data.type.Furnace;
import org.jetbrains.annotations.*;

public class FurnaceBlockTemplate extends AbstractBlockTemplate<Furnace> {

    private final BlockFace facing;
    private final boolean lit;

    public FurnaceBlockTemplate(@NotNull Material type, @NotNull BlockFace facing, boolean lit) {
        super(type, Furnace.class);
        this.facing = facing;
        this.lit = lit;
    }

    @Override
    public boolean matchBlockData(@NotNull Furnace blockData, @NotNull Rotation rotation) {
        return blockData.getFacing() == rotation.translateBlockFace(facing) &&
                blockData.isLit() == lit;
    }

    @Override
    public void updateBlockData(@NotNull Furnace blockData, @NotNull Rotation rotation) {
        blockData.setFacing(rotation.translateBlockFace(facing));
        blockData.setLit(lit);
    }
}
