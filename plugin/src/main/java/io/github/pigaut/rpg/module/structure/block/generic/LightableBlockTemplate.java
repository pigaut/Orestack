package io.github.pigaut.rpg.module.structure.block.generic;

import io.github.pigaut.rpg.core.transform.Rotation;
import io.github.pigaut.rpg.module.structure.block.*;
import io.github.pigaut.rpg.module.structure.block.*;
import org.bukkit.*;
import org.bukkit.block.data.*;
import org.jetbrains.annotations.*;

public class LightableBlockTemplate extends AbstractBlockTemplate<Lightable> {

    private final boolean lit;

    public LightableBlockTemplate(@NotNull Material type, boolean lit) {
        super(type, Lightable.class);
        this.lit = lit;
    }

    @Override
    public boolean matchBlockData(@NotNull Lightable blockData, @NotNull Rotation rotation) {
        return blockData.isLit() == lit;
    }

    @Override
    public void updateBlockData(@NotNull Lightable blockData, @NotNull Rotation rotation) {
        blockData.setLit(lit);
    }
}
