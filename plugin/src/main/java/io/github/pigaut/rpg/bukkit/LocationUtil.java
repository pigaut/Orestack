package io.github.pigaut.rpg.bukkit;

import io.github.pigaut.rpg.server.Server;
import io.github.pigaut.yaml.util.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.*;

import java.util.*;

public class LocationUtil {

    public static @NotNull Collection<Player> getNearbyPlayers(@NotNull Location location, double radius) {
        World world = location.getWorld();
        if (world == null) {
            throw new IllegalArgumentException("Location has no world");
        }
        return world.getNearbyEntitiesByType(Player.class, location, radius, radius, radius, null);
    }

    public static boolean isInBounds(@NotNull Location location) {
        World world = location.getWorld();
        if (world == null) {
            return false;
        }

        double y = location.getY();
        if (y < world.getMinHeight() || y >= world.getMaxHeight()) {
            return false;
        }

        return world.getWorldBorder().isInside(location);
    }

    public static @NotNull Location centered(@NotNull Location location) {
        Location centerLoc = location.clone();
        centerLoc.setX(location.getBlockX() + 0.5);
        centerLoc.setY(location.getBlockY() + 0.5);
        centerLoc.setZ(location.getBlockZ() + 0.5);
        return centerLoc;
    }

    public static @NotNull World getWorldOrDefault(@NotNull Location location) {
        World world = location.getWorld();
        return world != null ? world : Server.getDefaultWorld();
    }

    public static @Nullable String getWorldName(@NotNull UUID worldId) {
        World world = Bukkit.getWorld(worldId);
        return world != null ? world.getName() : null;
    }

    public static void setDefaultWorldIfMissing(@NotNull Location location) {
        if (location.getWorld() == null) {
            location.setWorld(Server.getDefaultWorld());
        }
    }

    public static boolean matches(@NotNull Location location, @NotNull World world, int x, int y, int z) {
        World otherWord = location.getWorld();
        Preconditions.checkNotNull(otherWord, "Location must have a valid world.");
        return world.equals(otherWord) &&
                x == location.getBlockX() &&
                y == location.getBlockY() &&
                z == location.getBlockZ();
    }

    public static Location getOffsetLocation(Location location, double right, double up, double front) {
        Location result = location.clone();
        Vector direction = result.getDirection().normalize();

        Vector rightVector;
        if (Math.abs(direction.getY()) > 0.999) {
            rightVector = new Vector(1, 0, 0);
        } else {
            rightVector = direction.getCrossProduct(new Vector(0, 1, 0)).normalize();
        }

        Vector upVector = new Vector(0, 1, 0);

        Vector offset = new Vector()
                .add(rightVector.clone().multiply(right))
                .add(upVector.clone().multiply(up))
                .add(direction.clone().multiply(front));

        return result.add(offset);
    }

}
