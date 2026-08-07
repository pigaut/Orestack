package io.github.pigaut.rpg.hook.craftengine;

import io.github.pigaut.rpg.core.transform.Rotation;
import io.github.pigaut.rpg.module.structure.block.*;
import io.github.pigaut.rpg.module.structure.block.*;
import net.momirealms.craftengine.bukkit.api.*;
import net.momirealms.craftengine.core.block.*;
import org.bukkit.*;
import org.jetbrains.annotations.*;

public class CraftEngineBlockTemplate implements BlockTemplate {

    private final CustomBlock customBlock;

    public CraftEngineBlockTemplate(@NotNull CustomBlock customBlock) {
        this.customBlock = customBlock;
    }

    @Override
    public boolean isPlaced(@NotNull Location location, @NotNull Rotation rotation) {
        return CraftEngineBlocks.isCustomBlock(location.getBlock());
    }

    @Override
    public void place(@NotNull Location location, @NotNull Rotation rotation) {
        CraftEngineBlocks.place(location, customBlock.defaultState(), false);
    }

    @Override
    public void remove(@NotNull Location location) {
        CraftEngineBlocks.remove(location.getBlock());
    }

}
