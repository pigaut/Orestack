package io.github.pigaut.orestack.generator;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.command.*;
import io.github.pigaut.orestack.listener.*;
import io.github.pigaut.sql.*;
import io.github.pigaut.voxel.command.*;
import io.github.pigaut.voxel.hologram.display.*;
import io.github.pigaut.voxel.plugin.manager.*;
import io.github.pigaut.voxel.yaml.*;
import io.github.pigaut.voxel.yaml.node.sequence.*;
import org.bukkit.*;
import org.bukkit.event.*;
import org.jetbrains.annotations.*;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class GeneratorManager extends Manager {

    private final OrestackPlugin plugin;
    private final Map<String, Generator> generatorsByName = new ConcurrentHashMap<>();
    private final Map<Location, BlockGenerator> generatorsByLocation = new ConcurrentHashMap<>();
    private @Nullable DataTable resourcesTable = null;

    public GeneratorManager(OrestackPlugin plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    public List<String> getGeneratorNames() {
        return new ArrayList<>(generatorsByName.keySet());
    }

    public Collection<Generator> getAllGenerators() {
        return new ArrayList<>(generatorsByName.values());
    }

    public Collection<BlockGenerator> getAllBlockGenerators() {
        return new ArrayList<>(generatorsByLocation.values());
    }

    public @Nullable Generator getGenerator(String name) {
        return name != null ? generatorsByName.get(name) : null;
    }

    public void addGenerator(@NotNull String name, @NotNull Generator generator) {
        generatorsByName.put(name, generator);
    }

    public @Nullable BlockGenerator getBlockGenerator(@NotNull Location location) {
        return generatorsByLocation.get(location);
    }

    public void addBlockGenerator(@NotNull BlockGenerator generator) {
        generatorsByLocation.put(generator.getLocation(), generator);
    }

    public void removeBlockGenerator(@NotNull Location location) {
        final BlockGenerator removedGenerator = generatorsByLocation.remove(location);
        if (removedGenerator != null) {
            location.getBlock().setType(Material.AIR);
            final HologramDisplay hologram = removedGenerator.getCurrentHologram();
            if (hologram != null && hologram.exists()) {
                hologram.despawn();
            }
        }
    }

    @Override
    public void disable() {
        for (BlockGenerator blockGenerator : generatorsByLocation.values()) {
            blockGenerator.cancelGrowth();
            final HologramDisplay hologramDisplay = blockGenerator.getCurrentHologram();
            if (hologramDisplay != null) {
                hologramDisplay.despawn();
            }
        }
    }

    @Override
    public void loadData() {
        generatorsByName.clear();
        for (File generatorFile : plugin.getFiles("generators")) {
            final RootSequence config = ConfigSequence.loadConfiguration(generatorFile, plugin.getConfigurator());
            generatorsByName.put(config.getName(), config.load(Generator.class));
        }
        generatorsByLocation.clear();
        resourcesTable = plugin.getDatabase().tableOf("resources");
        resourcesTable.createIfNotExists(
                "world VARCHAR(255)",
                "x INT NOT NULL",
                "y INT NOT NULL",
                "z INT NOT NULL",
                "generator VARCHAR(255) NOT NULL",
                "PRIMARY KEY (world, x, y, z)"
        );
        resourcesTable.selectAll().fetchAllRows(rowQuery -> {
            final String worldName = rowQuery.getString(1);
            final int x = rowQuery.getInt(2);
            final int y = rowQuery.getInt(3);
            final int z = rowQuery.getInt(4);
            final String generatorName = rowQuery.getString(5);
            final World world = Bukkit.getWorld(worldName);
            if (world == null) {
                return;
            }
            final Generator generator = getGenerator(generatorName);
            if (generator == null) {
                return;
            }
            final Location location = new Location(world, x, y, z);
            plugin.getScheduler().runTask(() -> BlockGenerator.create(generator, location));
        });
    }

    @Override
    public void saveData() {
        if (resourcesTable == null) {
            return;
        }
        resourcesTable.createIfNotExists(
                "world VARCHAR(255)",
                "x INT NOT NULL",
                "y INT NOT NULL",
                "z INT NOT NULL",
                "generator VARCHAR(255) NOT NULL",
                "PRIMARY KEY (world, x, y, z)"
        );
        resourcesTable.clear();
        generatorsByLocation.forEach((location, generator) -> {
            final String worldName = location.getWorld().getName();
            final String x = Integer.toString(location.getBlockX());
            final String y = Integer.toString(location.getBlockY());
            final String z = Integer.toString(location.getBlockZ());
            final String generatorName = generator.getGenerator().getName();

            resourcesTable.insertAll("'" + worldName + "'", x, y, z, "'" + generatorName + "'").executeUpdate();
        });
    }

    @Override
    public List<EnhancedCommand> getCommands() {
        return List.of(new OrestackCommand(plugin));
    }

    @Override
    public List<Listener> getListeners() {
        return List.of(
                new PlayerInteractListener(plugin),
                new BlockBreakListener(plugin),
                new BlockDestructionListener(plugin),
                new CropChangeListener(plugin),
                new ChunkLoadListener(plugin)
        );
    }

}
