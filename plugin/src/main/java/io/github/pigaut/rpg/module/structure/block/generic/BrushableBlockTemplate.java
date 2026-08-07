package io.github.pigaut.rpg.module.structure.block.generic;

import io.github.pigaut.rpg.core.transform.Rotation;
import io.github.pigaut.rpg.module.structure.block.*;
import io.github.pigaut.rpg.module.structure.block.*;
import org.bukkit.*;
import org.bukkit.block.data.*;
import org.jetbrains.annotations.*;

public class BrushableBlockTemplate extends AbstractBlockTemplate<Brushable> {

    private final int dusted;

    public BrushableBlockTemplate(@NotNull Material type, int dusted) {
        super(type, Brushable.class);
        this.dusted = dusted;
    }

    @Override
    public boolean matchBlockData(@NotNull Brushable blockData, @NotNull Rotation rotation) {
        return blockData.getDusted() == dusted;
    }

    @Override
    public void updateBlockData(@NotNull Brushable blockData, @NotNull Rotation rotation) {
        blockData.setDusted(dusted);
    }

}
