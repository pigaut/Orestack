package io.github.pigaut.rpg.bukkit;

import io.github.pigaut.yaml.util.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class BlockPosition {

    private final UUID worldId;
    private final int x, y, z;

    public BlockPosition(@NotNull UUID worldId, int x, int y, int z) {
        this.worldId = worldId;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static @NotNull BlockPosition fromBlock(@NotNull Block block) {
        return fromLocation(block.getLocation());
    }

    public static @NotNull BlockPosition fromLocation(@NotNull Location location) {
        World world = location.getWorld();
        Preconditions.checkNotNull(world, "Location must have a valid world.");
        return new BlockPosition(world.getUID(), location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    public boolean exists() {
        return getWorld() != null;
    }

    public @Nullable World getWorld() {
        return Bukkit.getWorld(worldId);
    }

    public @NotNull UUID getWorldId() {
        return worldId;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public @NotNull Location getLocation() {
        return new Location(Bukkit.getWorld(worldId), x, y, z);
    }

    public @NotNull Block getBlock() {
        Location location = getLocation();
        return location.getBlock();
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof BlockPosition that)) return false;
        return x == that.x && y == that.y && z == that.z && Objects.equals(worldId, that.worldId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(worldId, x, y, z);
    }

    @Override
    public String toString() {
        return "BlockPosition{" +
                "worldId=" + worldId +
                ", x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }

}
