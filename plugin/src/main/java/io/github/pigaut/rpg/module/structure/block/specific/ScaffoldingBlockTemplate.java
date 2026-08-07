package io.github.pigaut.rpg.module.structure.block.specific;

import io.github.pigaut.rpg.core.transform.Rotation;
import io.github.pigaut.rpg.module.structure.block.*;
import org.bukkit.*;
import org.bukkit.block.data.type.*;
import org.jetbrains.annotations.*;

public class ScaffoldingBlockTemplate extends AbstractBlockTemplate<Scaffolding> {

    private final boolean bottom;

    public ScaffoldingBlockTemplate(@NotNull Material type, boolean bottom) {
        super(type, Scaffolding.class);
        this.bottom = bottom;
    }

    @Override
    public boolean matchBlockData(@NotNull Scaffolding blockData, @NotNull Rotation rotation) {
        return blockData.isBottom() == bottom;
    }

    @Override
    public void updateBlockData(@NotNull Scaffolding blockData, @NotNull Rotation rotation) {
        blockData.setBottom(bottom);
    }
}
