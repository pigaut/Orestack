package io.github.pigaut.orestack.structure;

import org.bukkit.*;
import org.bukkit.block.*;
import io.github.pigaut.voxel.util.Rotation;
import org.jetbrains.annotations.*;

import java.util.*;

public class BlockStructure {

    private final List<BlockChange> blockChanges;

    public BlockStructure(List<BlockChange> blockChanges) {
        this.blockChanges = blockChanges;
    }

    public List<BlockChange> getBlockChanges() {
        return new ArrayList<>(blockChanges);
    }

    public boolean matchBlocks(Location origin, Rotation rotation) {
        for (BlockChange blockChange : blockChanges) {
            if (!blockChange.matchBlock(origin, rotation)) {
                return false;
            }
        }
        return true;
    }

    public @Nullable Block getBlockAt(Location origin, Rotation rotation, Location location) {
        for (BlockChange blockChange : blockChanges) {
            final Block block = blockChange.getBlock(origin, rotation);
            if (block.getLocation().equals(location)) {
                return block;
            }
        }
        return null;
    }

    public List<Block> getBlocks(Location origin, Rotation rotation) {
        return blockChanges.stream()
                .map(component -> component.getBlock(origin, rotation))
                .toList();
    }

    public void updateBlocks(Location origin, Rotation rotation) {
        for (BlockChange blockChange : blockChanges) {
            blockChange.updateBlock(origin, rotation);
        }
    }

    public void removeBlocks(Location origin, Rotation rotation) {
        for (BlockChange blockChange : blockChanges) {
            blockChange.removeBlock(origin, rotation);
        }
    }

}
