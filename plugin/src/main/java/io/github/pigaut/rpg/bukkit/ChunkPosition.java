package io.github.pigaut.rpg.bukkit;

import io.github.pigaut.yaml.util.*;
import org.bukkit.*;
import org.jetbrains.annotations.*;

public record ChunkPosition(String world, int x, int z) {

    public static @NotNull ChunkPosition of(@NotNull World world, int worldX, int worldZ) {
        return new ChunkPosition(world.getName(), worldX >> 4, worldZ >> 4);
    }

    public static @NotNull ChunkPosition fromLocation(@NotNull Location location) {
        World world = location.getWorld();
        Preconditions.checkArgument(world != null, "Location must have a world.");
        return new ChunkPosition(world.getName(), location.getBlockX() >> 4, location.getBlockZ() >> 4);
    }

    public static @NotNull ChunkPosition fromChunk(@NotNull Chunk chunk) {
        World world = chunk.getWorld();
        return new ChunkPosition(world.getName(), chunk.getX(), chunk.getZ());
    }

}
