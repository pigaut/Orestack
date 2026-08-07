package io.github.pigaut.rpg.module.structure.block.specific;

import io.github.pigaut.rpg.core.transform.Rotation;
import io.github.pigaut.rpg.module.structure.block.*;
import org.bukkit.*;
import org.bukkit.block.data.type.*;
import org.jetbrains.annotations.*;

public class SlabBlockTemplate extends AbstractBlockTemplate<Slab> {

    private final Slab.Type slabType;

    public SlabBlockTemplate(@NotNull Material type, Slab.Type slabType) {
        super(type, Slab.class);
        this.slabType = slabType;
    }

    @Override
    public boolean matchBlockData(@NotNull Slab blockData, @NotNull Rotation rotation) {
        return blockData.getType() == slabType;
    }

    @Override
    public void updateBlockData(@NotNull Slab blockData, @NotNull Rotation rotation) {
        blockData.setType(slabType);
    }
}
