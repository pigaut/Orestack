package io.github.pigaut.rpg.module.structure.block.generic;

import io.github.pigaut.rpg.core.transform.Rotation;
import io.github.pigaut.rpg.module.structure.block.*;
import io.github.pigaut.rpg.module.structure.block.*;
import org.bukkit.*;
import org.bukkit.block.data.*;
import org.jetbrains.annotations.*;

public class HangableBlockTemplate extends AbstractBlockTemplate<Hangable> {

    private final boolean hanging;

    public HangableBlockTemplate(@NotNull Material type, boolean hanging) {
        super(type, Hangable.class);
        this.hanging = hanging;
    }

    @Override
    public boolean matchBlockData(@NotNull Hangable blockData, @NotNull Rotation rotation) {
        return blockData.isHanging() == hanging;
    }

    @Override
    public void updateBlockData(@NotNull Hangable blockData, @NotNull Rotation rotation) {
        blockData.setHanging(hanging);
    }
}
