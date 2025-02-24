package io.github.pigaut.orestack.generator;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.generator.template.*;
import io.github.pigaut.orestack.structure.*;
import io.github.pigaut.orestack.util.*;
import io.github.pigaut.sql.*;
import io.github.pigaut.voxel.hologram.display.*;
import io.github.pigaut.voxel.plugin.manager.*;
import io.github.pigaut.voxel.util.Rotation;
import io.github.pigaut.yaml.parser.deserializer.*;
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
            for (Block block : blockGenerator.getAllOccupiedBlocks()) {
                block.setType(Material.AIR);
            }
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
                "rotation VARCHAR(5) NOT NULL",
                "stage INT NOT NULL",
                "PRIMARY KEY (world, x, y, z)"
        );

        resourcesTable.selectAll().fetchAllRows(rowQuery -> {
            final String worldId = rowQuery.getString(1);
            final int x = rowQuery.getInt(2);
            final int y = rowQuery.getInt(3);
            final int z = rowQuery.getInt(4);
            final String generatorName = rowQuery.getString(5);
            final String rotationData = rowQuery.getString(6);
            final int stage = rowQuery.getInt(7);

            final World world = Bukkit.getWorld(UUID.fromString(worldId));
            if (world == null) {
                plugin.getLogger().warning("Removed generator at " + world.getName() + ", " + x + ", " + y + ", " + z + ". " +
                        "Reason: world does not exist.");
                return;
            }

            final GeneratorTemplate template = plugin.getGeneratorTemplate(generatorName);
            if (template == null) {
                plugin.getLogger().warning("Removed generator at " + world.getName() + ", " + x + ", " + y + ", " + z + ". " +
                        "Reason: generator does not exist.");
                return;
            }

            final Location origin = new Location(world, x, y, z);
            final Rotation rotation = Deserializers.getEnum(Rotation.class, rotationData);

            if (rotation == null) {
                plugin.getLogger().warning("Removed generator at " + world.getName() + ", " + x + ", " + y + ", " + z + ". " +
                        "Reason: invalid rotation data.");
                return;
            }

            final int maxStage = template.getMaxStage();
            if (stage > maxStage) {
                plugin.getLogger().warning("Failed to load saved generator stage. Reason: " + template.getName() +
                        " generator supports a maximum of " + maxStage + " stages.");
            }

            plugin.getScheduler().runTask(() -> {
                try {
                    Generator.create(template, origin, rotation, Math.min(stage, maxStage));
                } catch (GeneratorOverlapException e) {
                    plugin.getLogger().warning("Removed generator at " + world.getName() + ", " + x + ", " + y + ", " + z + ". " +
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
                "rotation VARCHAR(5)",
                "stage INT NOT NULL",
                "PRIMARY KEY (world, x, y, z)"
        );
        resourcesTable.clear();
        final DatabaseStatement insertStatement =
                resourcesTable.insertInto("world", "x", "y", "z", "generator", "rotation", "stage");
        for (Generator generator : generators) {
            final Location location = generator.getOrigin();
            insertStatement.withParameter(location.getWorld().getUID().toString());
            insertStatement.withParameter(location.getBlockX());
            insertStatement.withParameter(location.getBlockY());
            insertStatement.withParameter(location.getBlockZ());
            insertStatement.withParameter(generator.getTemplate().getName());
            insertStatement.withParameter(generator.getRotation().toString());
            insertStatement.withParameter(generator.getCurrentStageId());
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
        structure.removeBlocks(generator.getOrigin(), generator.getRotation());

        final HologramDisplay hologram = generator.getCurrentHologram();
        if (hologram != null && hologram.exists()) {
            hologram.despawn();
        }
    }

}
