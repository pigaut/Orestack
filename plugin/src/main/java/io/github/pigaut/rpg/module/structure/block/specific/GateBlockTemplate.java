package io.github.pigaut.rpg.module.structure.block.specific;

import io.github.pigaut.rpg.core.transform.Rotation;
import io.github.pigaut.rpg.module.structure.block.*;
import io.github.pigaut.rpg.module.structure.block.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.block.data.type.*;
import org.jetbrains.annotations.*;

public class GateBlockTemplate extends io.github.pigaut.rpg.module.structure.block.AbstractBlockTemplate<Gate> {

    private final BlockFace facing;
    private final boolean open;
    private final boolean inWall;

    public GateBlockTemplate(@NotNull Material type, @NotNull BlockFace facing,
                             boolean open, boolean inWall) {
        super(type, Gate.class);
        this.facing = facing;
        this.open = open;
        this.inWall = inWall;
    }

    @Override
    public boolean matchBlockData(@NotNull Gate blockData, @NotNull Rotation rotation) {
        return blockData.getFacing() == rotation.translateBlockFace(facing) &&
                blockData.isOpen() == open &&
                blockData.isInWall() == inWall;
    }

    @Override
    public void updateBlockData(@NotNull Gate blockData, @NotNull Rotation rotation) {
        blockData.setFacing(rotation.translateBlockFace(facing));
        blockData.setOpen(open);
        blockData.setInWall(inWall);
    }
}
