package io.github.pigaut.rpg.module.structure.block.generic;

import io.github.pigaut.rpg.core.transform.Rotation;
import io.github.pigaut.rpg.module.structure.block.*;
import io.github.pigaut.rpg.module.structure.block.*;
import org.bukkit.*;
import org.bukkit.block.data.*;
import org.jetbrains.annotations.*;

public class BisectedBlockTemplate extends AbstractBlockTemplate<Bisected> {

    private final Bisected.Half half;

    public BisectedBlockTemplate(@NotNull Material type, @NotNull Bisected.Half half) {
        super(type, Bisected.class);
        this.half = half;
    }

    @Override
    public boolean matchBlockData(@NotNull Bisected blockData, @NotNull Rotation rotation) {
        return blockData.getHalf() == half;
    }

    @Override
    public void updateBlockData(@NotNull Bisected blockData, @NotNull Rotation rotation) {
        blockData.setHalf(half);
    }
}
