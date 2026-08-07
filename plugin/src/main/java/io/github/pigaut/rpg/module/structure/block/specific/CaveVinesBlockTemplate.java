package io.github.pigaut.rpg.module.structure.block.specific;

import io.github.pigaut.rpg.core.transform.Rotation;
import io.github.pigaut.rpg.module.structure.block.*;
import io.github.pigaut.rpg.module.structure.block.*;
import org.bukkit.*;
import org.bukkit.block.data.type.*;
import org.jetbrains.annotations.*;

public class CaveVinesBlockTemplate extends AbstractBlockTemplate<CaveVines> {

    private final boolean berries;

    public CaveVinesBlockTemplate(Material type, boolean berries) {
        super(type, CaveVines.class);
        this.berries = berries;
    }

    @Override
    public boolean matchBlockData(@NotNull CaveVines blockData, @NotNull Rotation rotation) {
        return blockData.isBerries() == berries;
    }

    @Override
    public void updateBlockData(@NotNull CaveVines blockData, @NotNull Rotation rotation) {
        blockData.setBerries(berries);
    }
}
