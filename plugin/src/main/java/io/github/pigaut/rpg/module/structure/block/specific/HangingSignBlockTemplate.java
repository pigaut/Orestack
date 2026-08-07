package io.github.pigaut.rpg.module.structure.block.specific;

import io.github.pigaut.rpg.core.transform.Rotation;
import io.github.pigaut.rpg.module.structure.block.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.block.data.type.HangingSign;
import org.jetbrains.annotations.*;

public class HangingSignBlockTemplate extends AbstractBlockTemplate<HangingSign> {

    private final BlockFace facing;
    private final boolean attached;

    public HangingSignBlockTemplate(@NotNull Material type, @NotNull BlockFace facing, boolean attached) {
        super(type, HangingSign.class);
        this.facing = facing;
        this.attached = attached;
    }

    @Override
    public boolean matchBlockData(@NotNull HangingSign blockData, @NotNull Rotation rotation) {
        return blockData.getRotation() == rotation.translateBlockFace(facing) &&
                blockData.isAttached() == attached;
    }

    @Override
    public void updateBlockData(@NotNull HangingSign blockData, @NotNull Rotation rotation) {
        blockData.setRotation(rotation.translateBlockFace(facing));
        blockData.setAttached(attached);
    }
}
