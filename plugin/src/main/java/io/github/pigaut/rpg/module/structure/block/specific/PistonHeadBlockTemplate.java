package io.github.pigaut.rpg.module.structure.block.specific;

import io.github.pigaut.rpg.core.transform.Rotation;
import io.github.pigaut.rpg.module.structure.block.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.block.data.type.*;
import org.jetbrains.annotations.*;

public class PistonHeadBlockTemplate extends AbstractBlockTemplate<PistonHead> {

    private final BlockFace facing;
    private final boolean shortHead;

    public PistonHeadBlockTemplate(@NotNull Material type, BlockFace facing, boolean shortHead) {
        super(type, PistonHead.class);
        this.facing = facing;
        this.shortHead = shortHead;
    }

    @Override
    public boolean matchBlockData(@NotNull PistonHead blockData, @NotNull Rotation rotation) {
        return blockData.getFacing() == rotation.translateBlockFace(facing) &&
                blockData.isShort() == shortHead;
    }

    @Override
    public void updateBlockData(@NotNull PistonHead blockData, @NotNull Rotation rotation) {
        blockData.setFacing(rotation.translateBlockFace(facing));
        blockData.setShort(shortHead);
    }
}
