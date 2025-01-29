package io.github.pigaut.orestack.generator;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.generator.template.*;
import io.github.pigaut.orestack.structure.*;
import io.github.pigaut.orestack.util.*;
import io.github.pigaut.sql.*;
import io.github.pigaut.voxel.hologram.display.*;
import io.github.pigaut.voxel.plugin.manager.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.jetbrains.annotations.*;

import java.util.*;
import java.util.concurrent.*;

public class GeneratorManager extends Manager {

    private final OrestackPlugin plugin;
    private final List<Generator> generators = new ArrayList<>();
    private final Map<Location, Generator> generatorBlocks = new ConcurrentHashMap<>();

    public GeneratorManager(OrestackPlugin plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @Override
    public void disable() {
        for (Generator blockGenerator : generators) {
            blockGenerator.cancelGrowth();
            final HologramDisplay hologramDisplay = blockGenerator.getCurrentHologram();
            if (hologramDisplay != null) {
                hologramDisplay.despawn();
            }
        }
    }

    @Override
    public void loadData() {
        generators.clear();
        generatorBlocks.clear();
        final DataTable resourcesTable = plugin.getDatabase().tableOf("resources");
        resourcesTable.createIfNotExists(
                "world VARCHAR(255)",
                "x INT NOT NULL",
                "y INT NOT NULL",
                "z INT NOT NULL",
                "generator VARCHAR(255) NOT NULL",
                "PRIMARY KEY (world, x, y, z)"
        );
        resourcesTable.selectAll().fetchAllRows(rowQuery -> {
            final String worldId = rowQuery.getString(1);
            final int x = rowQuery.getInt(2);
            final int y = rowQuery.getInt(3);
            final int z = rowQuery.getInt(4);
            final String generatorName = rowQuery.getString(5);
            final World world = Bukkit.getWorld(UUID.fromString(worldId));
            if (world == null) {
                return;
            }
            final GeneratorTemplate template = plugin.getGeneratorTemplate(generatorName);
            if (template == null) {
                return;
            }
            final Location location = new Location(world, x, y, z);
            plugin.getScheduler().runTask(() -> {
                try {
                    Generator.create(template, location);
                } catch (GeneratorOverlapException e) {
                    plugin.getLogger().severe("Removed generator at " + world.getName() + ", " + x + ", " + y + ", " + z + ". " +
                            "Reason: generators overlapped.");
                }
            });
        });
    }

    @Override
    public void saveData() {
        final DataTable resourcesTable = plugin.getDatabase().tableOf("resources");
        resourcesTable.createIfNotExists(
                "world VARCHAR(36)",
                "x INT NOT NULL",
                "y INT NOT NULL",
                "z INT NOT NULL",
                "generator VARCHAR(255) NOT NULL",
                "PRIMARY KEY (world, x, y, z)"
        );
        resourcesTable.clear();
        final DatabaseStatement insertStatement = resourcesTable.insertInto("world", "x", "y", "z", "generator");
        for (Generator generator : generators) {
            final Location location = generator.getOrigin();
            insertStatement.withParameter(location.getWorld().getUID().toString());
            insertStatement.withParameter(location.getBlockX());
            insertStatement.withParameter(location.getBlockY());
            insertStatement.withParameter(location.getBlockZ());
            insertStatement.withParameter(generator.getTemplate().getName());
            insertStatement.addBatch();
        }
        insertStatement.executeBatch();
    }

    @Override
    public boolean isAutoSave() {
        return true;
    }

    public Collection<Generator> getAllGenerators() {
        return new ArrayList<>(generators);
    }

    public boolean isGenerator(@NotNull Location location) {
        return generatorBlocks.containsKey(location);
    }

    public @Nullable Generator getGenerator(@NotNull Location location) {
        return generatorBlocks.get(location);
    }

    public void registerGenerator(@NotNull Generator generator) {
        generators.add(generator);
        for (Block block : generator.getAllOccupiedBlocks()) {
            generatorBlocks.put(block.getLocation(), generator);
        }
    }

    public void removeGenerator(@NotNull Generator generator) {
        generators.remove(generator);
        for (Block block : generator.getAllOccupiedBlocks()) {
            generatorBlocks.remove(block.getLocation());
        }
        final BlockStructure structure = generator.getCurrentStage().getStructure();
        structure.removeBlocks(generator.getOrigin());

        final HologramDisplay hologram = generator.getCurrentHologram();
        if (hologram != null && hologram.exists()) {
            hologram.despawn();
        }
    }

}
