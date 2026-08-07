package io.github.pigaut.rpg.module.structure.block.specific;

import io.github.pigaut.rpg.core.transform.Rotation;
import io.github.pigaut.rpg.module.structure.block.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.block.data.*;
import org.bukkit.block.data.type.*;
import org.jetbrains.annotations.*;

public class GrindstoneBlockTemplate extends AbstractBlockTemplate<Grindstone> {

    private final BlockFace facing;
    private final FaceAttachable.AttachedFace attachedFace;

    public GrindstoneBlockTemplate(@NotNull Material type, @NotNull BlockFace facing,
                                   @NotNull FaceAttachable.AttachedFace attachedFace) {
        super(type, Grindstone.class);
        this.facing = facing;
        this.attachedFace = attachedFace;
    }

    @Override
    public boolean matchBlockData(@NotNull Grindstone blockData, @NotNull Rotation rotation) {
        return blockData.getFacing() == rotation.translateBlockFace(facing) &&
                blockData.getAttachedFace() == attachedFace;
    }

    @Override
    public void updateBlockData(@NotNull Grindstone blockData, @NotNull Rotation rotation) {
        blockData.setFacing(rotation.translateBlockFace(facing));
        blockData.setAttachedFace(attachedFace);
    }
}
