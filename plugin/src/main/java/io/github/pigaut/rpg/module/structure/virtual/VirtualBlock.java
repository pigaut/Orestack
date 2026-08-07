package io.github.pigaut.rpg.module.structure.virtual;

import org.bukkit.*;
import org.bukkit.block.data.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

public class VirtualBlock {

    private static final BlockData EMPTY_BLOCK_DATA = Material.AIR.createBlockData();

    private final Location location;
    private final BlockData blockData;

    public VirtualBlock(@NotNull Location location, @NotNull BlockData blockData) {
        this.location = location;
        this.blockData = blockData;
    }

    public @NotNull Location getLocation() {
        return location;
    }

    public @NotNull Material getType() {
        return blockData.getMaterial();
    }

    public @NotNull BlockData getBlockData() {
        return blockData;
    }

    public void send(@NotNull Player player) {
        player.sendBlockChange(location, blockData);
    }

    public void remove(@NotNull Player player) {
        player.sendBlockChange(location, EMPTY_BLOCK_DATA);
    }

}
