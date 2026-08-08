package io.github.pigaut.rpg.hook.craftengine;

import io.github.pigaut.rpg.core.transform.Rotation;
import io.github.pigaut.rpg.module.structure.block.*;
import io.github.pigaut.rpg.module.structure.block.matcher.*;
import net.momirealms.craftengine.bukkit.api.*;
import net.momirealms.craftengine.core.block.*;
import net.momirealms.craftengine.core.util.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.jetbrains.annotations.*;

public class CraftEngineBlockTemplate implements BlockTemplate, BlockMatcher {

    private final BlockDefinition blockDefinition;

    public CraftEngineBlockTemplate(@NotNull BlockDefinition blockDefinition) {
        this.blockDefinition = blockDefinition;
    }

    @Override
    public boolean matchBlock(@NotNull Block block) {
        ImmutableBlockState state = CraftEngineBlocks.getCustomBlockState(block);
        if (state != null && !state.isEmpty()) {
            return state.owner().matchesKey(blockDefinition.id());
        }
        return false;
    }

    @Override
    public boolean isPlaced(@NotNull Location location, @NotNull Rotation rotation) {
        return matchBlock(location.getBlock());
    }

    @Override
    public void place(@NotNull Location location, @NotNull Rotation rotation) {
        CraftEngineBlocks.place(location, blockDefinition.defaultState(), false);
    }

    @Override
    public void remove(@NotNull Location location) {
        CraftEngineBlocks.remove(location.getBlock());
    }

}
