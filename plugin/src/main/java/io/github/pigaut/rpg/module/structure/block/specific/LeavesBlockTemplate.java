package io.github.pigaut.rpg.module.structure.block.specific;

import io.github.pigaut.rpg.core.transform.Rotation;
import io.github.pigaut.rpg.module.structure.block.*;
import org.bukkit.*;
import org.bukkit.block.data.type.*;
import org.jetbrains.annotations.*;

public class LeavesBlockTemplate extends AbstractBlockTemplate<Leaves> {

    public LeavesBlockTemplate(@NotNull Material type, @NotNull Class<Leaves> blockData) {
        super(type, blockData);
    }

    @Override
    public boolean matchBlockData(@NotNull Leaves blockData, @NotNull Rotation rotation) {
        return true;
    }

    @Override
    public void updateBlockData(@NotNull Leaves blockData, @NotNull Rotation rotation) {
        blockData.setPersistent(true);
    }
}
