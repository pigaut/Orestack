package io.github.pigaut.rpg.module.structure.block.generic;

import io.github.pigaut.rpg.core.transform.Rotation;
import io.github.pigaut.rpg.module.structure.block.*;
import io.github.pigaut.rpg.module.structure.block.*;
import org.bukkit.*;
import org.bukkit.block.data.*;
import org.jetbrains.annotations.*;

public class WaterloggedBlockTemplate extends AbstractBlockTemplate<Waterlogged> {

    private final boolean waterlogged;

    public WaterloggedBlockTemplate(@NotNull Material type, boolean waterlogged) {
        super(type, Waterlogged.class);
        this.waterlogged = waterlogged;
    }

    @Override
    public boolean matchBlockData(@NotNull Waterlogged blockData, @NotNull Rotation rotation) {
        return blockData.isWaterlogged() == waterlogged;
    }

    @Override
    public void updateBlockData(@NotNull Waterlogged blockData, @NotNull Rotation rotation) {
        blockData.setWaterlogged(waterlogged);
    }
}
