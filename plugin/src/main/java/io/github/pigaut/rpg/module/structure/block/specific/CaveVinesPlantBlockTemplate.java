package io.github.pigaut.rpg.module.structure.block.specific;

import io.github.pigaut.rpg.core.transform.Rotation;
import io.github.pigaut.rpg.module.structure.block.*;
import io.github.pigaut.rpg.module.structure.block.*;
import org.bukkit.*;
import org.bukkit.block.data.type.*;
import org.jetbrains.annotations.*;

public class CaveVinesPlantBlockTemplate extends AbstractBlockTemplate<CaveVinesPlant> {

    private final boolean berries;

    public CaveVinesPlantBlockTemplate(Material type, boolean berries) {
        super(type, CaveVinesPlant.class);
        this.berries = berries;
    }

    @Override
    public boolean matchBlockData(@NotNull CaveVinesPlant blockData, @NotNull Rotation rotation) {
        return blockData.isBerries() == berries;
    }

    @Override
    public void updateBlockData(@NotNull CaveVinesPlant blockData, @NotNull Rotation rotation) {
        blockData.setBerries(berries);
    }
}
