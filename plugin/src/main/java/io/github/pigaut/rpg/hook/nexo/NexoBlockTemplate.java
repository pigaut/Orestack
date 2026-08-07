package io.github.pigaut.rpg.hook.nexo;

import com.nexomc.nexo.api.*;
import io.github.pigaut.rpg.core.transform.Rotation;
import io.github.pigaut.rpg.module.structure.block.*;
import io.github.pigaut.rpg.module.structure.block.*;
import org.bukkit.*;
import org.jetbrains.annotations.*;

public class NexoBlockTemplate implements BlockTemplate {

    private final String blockId;

    public NexoBlockTemplate(@NotNull String blockId) {
        this.blockId = blockId;
    }

    @Override
    public boolean isPlaced(@NotNull Location location, @NotNull Rotation rotation) {
        return NexoBlocks.isCustomBlock(location.getBlock());
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
