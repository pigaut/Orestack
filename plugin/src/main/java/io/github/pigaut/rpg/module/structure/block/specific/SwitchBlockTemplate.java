package io.github.pigaut.rpg.module.structure.block.specific;

import io.github.pigaut.rpg.core.transform.Rotation;
import io.github.pigaut.rpg.module.structure.block.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.block.data.*;
import org.bukkit.block.data.type.*;
import org.jetbrains.annotations.*;

public class SwitchBlockTemplate extends AbstractBlockTemplate<Switch> {

    private final BlockFace facing;
    private final FaceAttachable.AttachedFace attachedFace;
    private final boolean powered;

    public SwitchBlockTemplate(@NotNull Material type, @NotNull BlockFace facing,
                               @NotNull FaceAttachable.AttachedFace attachedFace, boolean powered) {
        super(type, Switch.class);
        this.facing = facing;
        this.attachedFace = attachedFace;
        this.powered = powered;
    }

    @Override
    public boolean matchBlockData(@NotNull Switch blockData, @NotNull Rotation rotation) {
        return blockData.getFacing() == rotation.translateBlockFace(facing) &&
                blockData.getAttachedFace() == attachedFace &&
                blockData.isPowered() == powered;
    }

    @Override
    public void updateBlockData(@NotNull Switch blockData, @NotNull Rotation rotation) {
        blockData.setFacing(rotation.translateBlockFace(facing));
        blockData.setAttachedFace(attachedFace);
        blockData.setPowered(powered);
    }
}
