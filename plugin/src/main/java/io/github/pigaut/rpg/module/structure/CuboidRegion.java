package io.github.pigaut.rpg.module.structure;

import org.bukkit.*;

import java.util.*;

public class CuboidRegion {

    public static List<Location> getAllLocations(World world, Location firstPoint, Location secondPoint) {
        return CuboidRegion.getAllLocations(world,
                firstPoint.getBlockX(), firstPoint.getBlockY(), firstPoint.getBlockZ(),
                secondPoint.getBlockX(), secondPoint.getBlockY(), secondPoint.getBlockZ());
    }

    public static List<Location> getAllLocations(World world, int x1, int y1, int z1, int x2, int y2, int z2) {
        final int minX = Math.min(x1, x2);
        final int maxX = Math.max(x1, x2);
        final int minY = Math.min(y1, y2);
        final int maxY = Math.max(y1, y2);
        final int minZ = Math.min(z1, z2);
        final int maxZ = Math.max(z1, z2);

        final List<Location> locations = new ArrayList<>();
        for (int x = maxX; x >= minX; x--) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = maxZ; z >= minZ; z--) {
                    locations.add(new Location(world, x, y, z));
                }
            }
        }
        return locations;
    }

}
