package io.github.pigaut.rpg.hook.itemsadder;

import dev.lone.itemsadder.api.*;
import io.github.pigaut.rpg.core.transform.Rotation;
import io.github.pigaut.rpg.module.structure.block.*;
import io.github.pigaut.rpg.module.structure.block.*;
import io.github.pigaut.rpg.module.structure.block.matcher.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.jetbrains.annotations.*;

public class ItemsAdderBlockTemplate implements BlockTemplate, BlockMatcher {

    private final CustomBlock customBlock;

    public ItemsAdderBlockTemplate(@NotNull CustomBlock customBlock) {
        this.customBlock = customBlock;
    }

    @Override
    public boolean matchBlock(@NotNull Block block) {
        CustomBlock placedBlock = CustomBlock.byAlreadyPlaced(block);
        if (placedBlock == null) {
            return false;
        }
        return customBlock.getNamespacedID().equals(placedBlock.getNamespacedID());
    }

    @Override
    public boolean isPlaced(@NotNull Location location, @NotNull Rotation rotation) {
        return matchBlock(location.getBlock());
    }

    @Override
    public void place(@NotNull Location location, @NotNull Rotation rotation) {
        customBlock.place(location);
    }

    @Override
    public void remove(@NotNull Location location) {
        CustomBlock.remove(location);
    }

}
