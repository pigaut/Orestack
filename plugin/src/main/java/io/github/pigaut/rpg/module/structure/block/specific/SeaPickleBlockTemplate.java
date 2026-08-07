package io.github.pigaut.rpg.module.structure.block.specific;

import io.github.pigaut.rpg.core.transform.Rotation;
import io.github.pigaut.rpg.module.structure.block.*;
import org.bukkit.*;
import org.bukkit.block.data.type.*;
import org.jetbrains.annotations.*;

public class SeaPickleBlockTemplate extends AbstractBlockTemplate<SeaPickle> {

    private final int pickles;
    private final boolean waterlogged;

    public SeaPickleBlockTemplate(@NotNull Material type, int pickles, boolean waterlogged) {
        super(type, SeaPickle.class);
        this.pickles = pickles;
        this.waterlogged = waterlogged;
    }

    @Override
    public boolean matchBlockData(@NotNull SeaPickle blockData, @NotNull Rotation rotation) {
        return blockData.getPickles() == pickles &&
                blockData.isWaterlogged() == waterlogged;
    }

    @Override
    public void updateBlockData(@NotNull SeaPickle blockData, @NotNull Rotation rotation) {
        blockData.setPickles(pickles);
        blockData.setWaterlogged(waterlogged);
    }
}
