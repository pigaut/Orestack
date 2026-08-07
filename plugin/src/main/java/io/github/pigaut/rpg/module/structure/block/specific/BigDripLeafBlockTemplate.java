package io.github.pigaut.rpg.module.structure.block.specific;

import io.github.pigaut.rpg.core.transform.Rotation;
import io.github.pigaut.rpg.module.structure.block.*;
import io.github.pigaut.rpg.module.structure.block.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.block.data.type.*;
import org.jetbrains.annotations.*;

public class BigDripLeafBlockTemplate extends AbstractBlockTemplate<BigDripleaf> {

    private final BlockFace facing;
    private final BigDripleaf.Tilt tilt;

    public BigDripLeafBlockTemplate(@NotNull Material type, @NotNull BlockFace facing,
                                    @NotNull BigDripleaf.Tilt tilt) {
        super(type, BigDripleaf.class);
        this.facing = facing;
        this.tilt = tilt;
    }

    @Override
    public boolean matchBlockData(@NotNull BigDripleaf blockData, @NotNull Rotation rotation) {
        return blockData.getFacing() == rotation.translateBlockFace(facing) &&
                blockData.getTilt() == tilt;
    }

    @Override
    public void updateBlockData(@NotNull BigDripleaf blockData, @NotNull Rotation rotation) {
        blockData.setFacing(rotation.translateBlockFace(facing));
        blockData.setTilt(tilt);
    }

}
