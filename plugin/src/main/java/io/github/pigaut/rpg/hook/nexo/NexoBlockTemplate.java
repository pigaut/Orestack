package io.github.pigaut.rpg.hook.nexo;

import com.nexomc.nexo.api.*;
import com.nexomc.nexo.mechanics.custom_block.*;
import io.github.pigaut.rpg.core.transform.Rotation;
import io.github.pigaut.rpg.module.structure.block.*;
import io.github.pigaut.rpg.module.structure.block.*;
import io.github.pigaut.rpg.module.structure.block.matcher.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.jetbrains.annotations.*;

public class NexoBlockTemplate implements BlockTemplate, BlockMatcher {

    private final String blockId;

    public NexoBlockTemplate(@NotNull String blockId) {
        this.blockId = blockId;
    }

    @Override
    public boolean matchBlock(@NotNull Block block) {
        CustomBlockMechanic mechanic = NexoBlocks.customBlockMechanic(block);
        if (mechanic != null) {
            return blockId.equals(mechanic.getItemID());
        }
        return false;
    }

    @Override
    public boolean isPlaced(@NotNull Location location, @NotNull Rotation rotation) {
        return matchBlock(location.getBlock());
    }

    @Override
    public void place(@NotNull Location location, @NotNull Rotation rotation) {
        NexoBlocks.place(blockId, location);
    }

    @Override
    public void remove(@NotNull Location location) {
        try {
            NexoBlocks.remove(location, null, false);
        } catch (IllegalStateException ignored) {
            // temporary fix
        }
    }

}
