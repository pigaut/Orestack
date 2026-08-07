package io.github.pigaut.rpg.module.structure.block.specific;

import io.github.pigaut.rpg.core.transform.Rotation;
import io.github.pigaut.rpg.module.structure.block.*;
import io.github.pigaut.rpg.module.structure.block.*;
import org.bukkit.*;
import org.bukkit.block.data.type.*;
import org.jetbrains.annotations.*;

public class BeehiveBlockTemplate extends AbstractBlockTemplate<Beehive> {

    private final int honeyLevel;

    public BeehiveBlockTemplate(@NotNull Material type, int honeyLevel) {
        super(type, Beehive.class);
        this.honeyLevel = honeyLevel;
    }

    @Override
    public boolean matchBlockData(@NotNull Beehive blockData, @NotNull Rotation rotation) {
        return blockData.getHoneyLevel() == honeyLevel;
    }

    @Override
    public void updateBlockData(@NotNull Beehive blockData, @NotNull Rotation rotation) {
        blockData.setHoneyLevel(honeyLevel);
    }

}
