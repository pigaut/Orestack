package io.github.pigaut.rpg.module.structure.block.specific;

import io.github.pigaut.rpg.core.transform.Rotation;
import io.github.pigaut.rpg.module.structure.block.*;
import io.github.pigaut.rpg.module.structure.block.*;
import org.bukkit.*;
import org.bukkit.block.data.type.*;
import org.jetbrains.annotations.*;

public class FarmlandBlockTemplate extends AbstractBlockTemplate<Farmland> {

    private final int moisture;

    protected FarmlandBlockTemplate(@NotNull Material type, int moisture) {
        super(type, Farmland.class);
        this.moisture = moisture;
    }

    @Override
    public boolean matchBlockData(@NotNull Farmland blockData, @NotNull Rotation rotation) {
        return blockData.getMoisture() == moisture;
    }

    @Override
    public void updateBlockData(@NotNull Farmland blockData, @NotNull Rotation rotation) {
        blockData.setMoisture(moisture);
    }
}
