package io.github.pigaut.rpg.module.structure.block.specific;

import io.github.pigaut.rpg.core.transform.Rotation;
import io.github.pigaut.rpg.module.structure.block.*;
import io.github.pigaut.rpg.module.structure.block.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.block.data.type.Chest;
import org.jetbrains.annotations.*;

public class ChestBlockTemplate extends AbstractBlockTemplate<Chest> {

    private final BlockFace facing;
    private final Chest.Type chestType;

    public ChestBlockTemplate(@NotNull Material type, @NotNull BlockFace facing,
                              @NotNull Chest.Type chestType) {
        super(type, Chest.class);
        this.facing = facing;
        this.chestType = chestType;
    }

    @Override
    public boolean matchBlockData(@NotNull Chest blockData, @NotNull Rotation rotation) {
        return blockData.getFacing() == rotation.translateBlockFace(facing) &&
                blockData.getType() == chestType;
    }

    @Override
    public void updateBlockData(@NotNull Chest blockData, @NotNull Rotation rotation) {
        blockData.setFacing(rotation.translateBlockFace(facing));
        blockData.setType(chestType);
    }
}
