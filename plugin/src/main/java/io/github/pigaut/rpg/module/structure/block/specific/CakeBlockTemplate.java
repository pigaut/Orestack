package io.github.pigaut.rpg.module.structure.block.specific;

import io.github.pigaut.rpg.core.transform.Rotation;
import io.github.pigaut.rpg.module.structure.block.*;
import io.github.pigaut.rpg.module.structure.block.*;
import org.bukkit.*;
import org.bukkit.block.data.type.*;
import org.jetbrains.annotations.*;

public class CakeBlockTemplate extends AbstractBlockTemplate<Cake> {

    private final int bites;

    public CakeBlockTemplate(@NotNull Material type, int bites) {
        super(type, Cake.class);
        this.bites = bites;
    }

    @Override
    public boolean matchBlockData(@NotNull Cake blockData, @NotNull Rotation rotation) {
        return blockData.getBites() == bites;
    }

    @Override
    public void updateBlockData(@NotNull Cake blockData, @NotNull Rotation rotation) {
        blockData.setBites(bites);
    }

}
