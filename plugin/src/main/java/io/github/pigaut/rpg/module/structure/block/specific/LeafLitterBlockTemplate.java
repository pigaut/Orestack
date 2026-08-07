package io.github.pigaut.rpg.module.structure.block.specific;

import io.github.pigaut.rpg.core.transform.Rotation;
import io.github.pigaut.rpg.module.structure.block.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.block.data.type.*;
import org.jetbrains.annotations.*;

public class LeafLitterBlockTemplate extends AbstractBlockTemplate<LeafLitter> {

    private final BlockFace facing;
    private final int segmentAmount;

    public LeafLitterBlockTemplate(@NotNull Material type, @NotNull BlockFace facing, int segmentAmount) {
        super(type, LeafLitter.class);
        this.facing = facing;
        this.segmentAmount = segmentAmount;
    }

    @Override
    public boolean matchBlockData(@NotNull LeafLitter blockData, @NotNull Rotation rotation) {
        return blockData.getFacing() == rotation.translateBlockFace(facing) &&
                blockData.getSegmentAmount() == segmentAmount;
    }

    @Override
    public void updateBlockData(@NotNull LeafLitter blockData, @NotNull Rotation rotation) {
        blockData.setFacing(rotation.translateBlockFace(facing));
        blockData.setSegmentAmount(segmentAmount);
    }
}
