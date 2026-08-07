package io.github.pigaut.rpg.core.playerblocks;

import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.manager.*;
import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.manager.*;
import org.bukkit.block.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class PlayerPlacedBlockManager extends Manager {

    private final Set<BlockPosition> playerPlacedBlocks;
    private final int maxCapacity = 50000;

    public PlayerPlacedBlockManager(@NotNull EnhancedJavaPlugin plugin) {
        super(plugin);
        Map<BlockPosition, Boolean> map = new LinkedHashMap<>(maxCapacity, 0.75f, false) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<BlockPosition, Boolean> eldest) {
                return size() > maxCapacity;
            }
        };
        this.playerPlacedBlocks = Collections.synchronizedSet(Collections.newSetFromMap(map));
    }

    public boolean isPlayerPlacedBlock(@NotNull Block block) {
        return playerPlacedBlocks.contains(BlockPosition.fromBlock(block));
    }

    public void add(@NotNull Block block) {
        playerPlacedBlocks.add(BlockPosition.fromBlock(block));
    }

    public void remove(@NotNull Block block) {
        playerPlacedBlocks.remove(BlockPosition.fromBlock(block));
    }
}
