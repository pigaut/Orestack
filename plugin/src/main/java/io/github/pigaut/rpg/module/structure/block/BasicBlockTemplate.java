package io.github.pigaut.rpg.module.structure.block;

import io.github.pigaut.rpg.core.transform.Rotation;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.block.data.*;
import org.jetbrains.annotations.*;

public class BasicBlockTemplate implements BlockTemplate {

    protected final Material type;

    public BasicBlockTemplate(Material type) {
        this.type = type;
    }

    public @NotNull Material getType() {
        return type;
    }

    @Override
    public boolean isPlaced(@NotNull Location location, @NotNull Rotation rotation) {
        Block block = location.getBlock();
        return block.getType() == type;
    }

    @Override
    public void remove(@NotNull Location location) {
        Block block = location.getBlock();
        if (block.getType() != Material.AIR) {
            block.setType(Material.AIR, false);
        }
    }

    @Override
    public void place(@NotNull Location location, @NotNull Rotation rotation) {
        Block block = location.getBlock();
        if (block.getType() != type) {
            block.setType(type, false);
        }
    }

    public @NotNull BlockData createBlockData(@NotNull Rotation rotation) {
        return type.createBlockData();
    }

}
