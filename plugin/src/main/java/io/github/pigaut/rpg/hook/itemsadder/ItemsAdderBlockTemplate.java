package io.github.pigaut.rpg.hook.itemsadder;

import dev.lone.itemsadder.api.*;
import io.github.pigaut.rpg.core.transform.Rotation;
import io.github.pigaut.rpg.module.structure.block.*;
import io.github.pigaut.rpg.module.structure.block.*;
import org.bukkit.*;
import org.jetbrains.annotations.*;

public class ItemsAdderBlockTemplate implements BlockTemplate {

    private final CustomBlock customBlock;

    public ItemsAdderBlockTemplate(@NotNull CustomBlock customBlock) {
        this.customBlock = customBlock;
    }

    @Override
    public boolean isPlaced(@NotNull Location location, @NotNull Rotation rotation) {
        CustomBlock placedBlock = CustomBlock.byAlreadyPlaced(location.getBlock());
        if (placedBlock == null) {
            return false;
        }
        return customBlock.getNamespacedID().equals(placedBlock.getNamespacedID());
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
