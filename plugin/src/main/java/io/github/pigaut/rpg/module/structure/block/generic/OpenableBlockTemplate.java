package io.github.pigaut.rpg.module.structure.block.generic;

import io.github.pigaut.rpg.core.transform.Rotation;
import io.github.pigaut.rpg.module.structure.block.*;
import io.github.pigaut.rpg.module.structure.block.*;
import org.bukkit.*;
import org.bukkit.block.data.*;
import org.jetbrains.annotations.*;

public class OpenableBlockTemplate extends AbstractBlockTemplate<Openable> {

    private final boolean open;

    public OpenableBlockTemplate(@NotNull Material type, boolean open) {
        super(type, Openable.class);
        this.open = open;
    }

    @Override
    public boolean matchBlockData(@NotNull Openable blockData, @NotNull Rotation rotation) {
        return blockData.isOpen() == open;
    }

    @Override
    public void updateBlockData(@NotNull Openable blockData, @NotNull Rotation rotation) {
        blockData.setOpen(open);
    }
}
