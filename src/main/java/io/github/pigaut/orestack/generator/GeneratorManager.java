package io.github.pigaut.orestack.generator;

import com.google.common.base.Preconditions;
import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.command.*;
import io.github.pigaut.orestack.listener.*;
import io.github.pigaut.sql.*;
import io.github.pigaut.voxel.command.*;
import io.github.pigaut.voxel.plugin.manager.*;
import io.github.pigaut.voxel.yaml.node.sequence.*;
import org.bukkit.*;
import org.bukkit.event.*;
import org.jetbrains.annotations.*;

import java.io.*;
import java.util.*;

public class GeneratorManager extends Manager {

    private final OrestackPlugin plugin;
    private final Map<String, Generator> generatorsByName = new HashMap<>();
    private final Map<Location, BlockGenerator> generatorsByLocation = new HashMap<>();
    private DataTable resourcesTable;

    public GeneratorManager(OrestackPlugin plugin) {
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
        return generatorsByName.get(name);
    }

    public void addGenerator(@NotNull String name, @NotNull Generator generator) {
        generatorsByName.put(name, generator);
    }

    public @Nullable BlockGenerator getBlockGenerator(@NotNull Location location) {
        return generatorsByLocation.get(location);
    }

    public void createBlockGenerator(@NotNull Generator generator, @NotNull Location location) {
        Preconditions.checkNotNull(location.getWorld(), "Location must have a valid world");
        final BlockGenerator blockGenerator = BlockGenerator.create(generator, location);
        generatorsByLocation.put(location, blockGenerator);
        blockGenerator.updateState();
    }

    public void removeBlockGenerator(@NotNull Location location) {
        generatorsByLocation.remove(location);
        location.getBlock().setType(Material.AIR);
    }

    @Override
    public void enable() {
        resourcesTable = plugin.getDatabase().tableOf("resources");
        this.load();
    }

    @Override
    public void disable() {
        this.save();
        for (BlockGenerator blockGenerator : generatorsByLocation.values()) {
            blockGenerator.cancelGrowth();
        }
        generatorsByName.clear();
        generatorsByLocation.clear();
    }

    @Override
    public void load() {
        for (File file : plugin.getFiles("generators")) {
            final RootSequence config = new RootSequence(file, plugin.getConfigurator());
            config.load();
            generatorsByName.put(config.getName(), config.load(Generator.class));
        }

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
            createBlockGenerator(generator, location);
        });
    }

    @Override
    public void save() {
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
                new CropChangeListener(plugin)
        );
    }

}
