package io.github.pigaut.rpg.module.structure.block.specific;

import io.github.pigaut.rpg.core.transform.Rotation;
import io.github.pigaut.rpg.module.structure.block.*;
import io.github.pigaut.rpg.module.structure.block.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.block.data.*;
import org.bukkit.block.data.type.*;
import org.jetbrains.annotations.*;

public class DoorBlockTemplate extends AbstractBlockTemplate<Door> {

    private final Bisected.Half half;
    private final BlockFace direction;
    private final Door.Hinge hinge;
    private final boolean open;

    public DoorBlockTemplate(@NotNull Material type, @NotNull Bisected.Half half,
                             @NotNull BlockFace direction, @NotNull Door.Hinge hinge, boolean open) {
        super(type, Door.class);
        this.half = half;
        this.direction = direction;
        this.hinge = hinge;
        this.open = open;
    }

    @Override
    public boolean matchBlockData(@NotNull Door blockData, @NotNull Rotation rotation) {
        return blockData.getHalf() == half &&
                blockData.getFacing() == rotation.translateBlockFace(direction) &&
                blockData.getHinge() == hinge &&
                blockData.isOpen() == open;
    }

    @Override
    public void updateBlockData(@NotNull Door blockData, @NotNull Rotation rotation) {
        blockData.setHalf(half);
        blockData.setFacing(rotation.translateBlockFace(direction));
        blockData.setHinge(hinge);
        blockData.setOpen(open);
    }
}
