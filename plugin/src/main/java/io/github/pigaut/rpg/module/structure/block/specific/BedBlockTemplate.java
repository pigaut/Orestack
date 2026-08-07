package io.github.pigaut.rpg.module.structure.block.specific;

import io.github.pigaut.rpg.core.transform.Rotation;
import io.github.pigaut.rpg.module.structure.block.*;
import io.github.pigaut.rpg.module.structure.block.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.block.data.type.Bed;
import org.jetbrains.annotations.*;

public class BedBlockTemplate extends AbstractBlockTemplate<Bed> {

    private final BlockFace facing;
    private final Bed.Part bedPart;

    public BedBlockTemplate(@NotNull Material type, @NotNull BlockFace facing,
                            @NotNull Bed.Part bedPart) {
        super(type, Bed.class);
        this.facing = facing;
        this.bedPart = bedPart;
    }

    @Override
    public boolean matchBlockData(@NotNull Bed blockData, @NotNull Rotation rotation) {
        return blockData.getFacing() == rotation.translateBlockFace(facing) &&
                blockData.getPart() == bedPart;
    }

    @Override
    public void updateBlockData(@NotNull Bed blockData, @NotNull Rotation rotation) {
        blockData.setFacing(rotation.translateBlockFace(facing));
        blockData.setPart(bedPart);
    }
}
