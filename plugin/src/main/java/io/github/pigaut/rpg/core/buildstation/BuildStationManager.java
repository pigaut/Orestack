package io.github.pigaut.rpg.core.buildstation;

import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.manager.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.manager.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class BuildStationManager extends Manager {

    private final Set<BuildStation> buildStations = new HashSet<>();
    private final Map<Location, BuildStation> buildStationBlocks = new HashMap<>();

    public BuildStationManager(EnhancedJavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public void enable() {
        for (BuildStation buildStation : buildStations) {
            buildStation.createStructure();
        }
    }

    @Override
    public void disable() {
        for (BuildStation buildStation : buildStations) {
            buildStation.removeStructure();
        }
    }

    public boolean registerBuildStation(@NotNull BuildStation buildStation) {
        if (buildStations.contains(buildStation)) {
            return false;
        }

        for (Block block : buildStation.getOccupiedBlocks()) {
            if (buildStationBlocks.containsKey(block.getLocation())) {
                return false;
            }
        }

        buildStations.add(buildStation);
        for (Block block : buildStation.getOccupiedBlocks()) {
            buildStationBlocks.put(block.getLocation(), buildStation);
        }
        return true;
    }

    public void unregisterBuildStation(@NotNull BuildStation buildStation) {
        buildStations.remove(buildStation);
        for (Block block : buildStation.getOccupiedBlocks()) {
            buildStationBlocks.remove(block.getLocation());
        }
    }

    public boolean isBuildStation(@NotNull Location location) {
        return buildStationBlocks.containsKey(location);
    }

    public @Nullable BuildStation getBuildStation(@NotNull Location location) {
        return buildStationBlocks.get(location);
    }

}
