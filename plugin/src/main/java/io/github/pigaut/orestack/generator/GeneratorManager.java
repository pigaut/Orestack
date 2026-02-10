package io.github.pigaut.orestack.generator;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.generator.template.*;
import io.github.pigaut.orestack.util.*;
import io.github.pigaut.sql.*;
import io.github.pigaut.voxel.*;
import io.github.pigaut.voxel.core.hologram.*;
import io.github.pigaut.voxel.core.structure.*;
import io.github.pigaut.voxel.plugin.manager.*;
import io.github.pigaut.yaml.convert.parse.*;
import io.github.pigaut.voxel.bukkit.Rotation;
import org.bukkit.*;
import org.bukkit.block.*;
import org.jetbrains.annotations.*;

import java.util.*;
import java.util.concurrent.*;

public class GeneratorManager extends Manager {

    private final OrestackPlugin plugin;
    private final Set<Generator> generators = new HashSet<>();
    private final Map<Location, Generator> generatorBlocks = new ConcurrentHashMap<>();
    private int largeGeneratorsPlaced = 0;

    public GeneratorManager(OrestackPlugin plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @Override
    public void disable() {
        for (Generator blockGenerator : generators) {
            blockGenerator.cancelGrowth();
            blockGenerator.removeBlocks();

            HologramDisplay hologramDisplay = blockGenerator.getCurrentHologram();
            if (hologramDisplay != null) {
                hologramDisplay.destroy();
            }
        }
    }

    @Override
    public void loadData() {
        generators.clear();
        generatorBlocks.clear();
        largeGeneratorsPlaced = 0;

        Database database = plugin.getDatabase();
        if (database == null) {
            logger.severe("Could not load data because database was not found.");
            return;
        }

        database.createTableIfNotExists("resources",
                "world VARCHAR(255)",
                "x INT NOT NULL",
                "y INT NOT NULL",
                "z INT NOT NULL",
                "generator VARCHAR(255) NOT NULL",
                "rotation VARCHAR(5) NOT NULL",
                "stage INT NOT NULL",
                "PRIMARY KEY (world, x, y, z)"
        );

        database.createTableIfNotExists("invalid_resources",
                "world VARCHAR(255)",
                "x INT NOT NULL",
                "y INT NOT NULL",
                "z INT NOT NULL",
                "generator VARCHAR(255) NOT NULL",
                "rotation VARCHAR(5) NOT NULL",
                "stage INT NOT NULL",
                "PRIMARY KEY (world, x, y, z)"
        );

        database.selectAll("resources").fetchAllRows(rowQuery -> {
            final String worldId = rowQuery.getString(1);
            final int x = rowQuery.getInt(2);
            final int y = rowQuery.getInt(3);
            final int z = rowQuery.getInt(4);
            final String generatorName = rowQuery.getString(5);
            final String rotationData = rowQuery.getString(6);
            final int stage = rowQuery.getInt(7);

            try {
                registerGenerator(worldId, x, y, z, generatorName, rotationData, stage);
            }
            catch (GeneratorCreateException e) {
                logger.warning(e.getMessage());
                database.merge("invalid_resources", "world, x, y, z",
                        "world", "x", "y", "z", "generator", "rotation", "stage")
                        .withParameter(worldId)
                        .withParameter(x)
                        .withParameter(y)
                        .withParameter(z)
                        .withParameter(generatorName)
                        .withParameter(rotationData)
                        .withParameter(stage)
                        .executeUpdate();
            }
        });

        database.selectAll("invalid_resources").fetchAllRows(rowQuery -> {
            final String worldId = rowQuery.getString(1);
            final int x = rowQuery.getInt(2);
            final int y = rowQuery.getInt(3);
            final int z = rowQuery.getInt(4);
            final String generatorName = rowQuery.getString(5);
            final String rotationData = rowQuery.getString(6);
            final int stage = rowQuery.getInt(7);

            try {
                registerGenerator(worldId, x, y, z, generatorName, rotationData, stage);
                logger.info(String.format("Restored generator at %s, %d, %d, %d. Reason: generator is no longer invalid.",
                    SpigotLibs.getWorldName(UUID.fromString(worldId)), x, y, z));
                database.createStatement("DELETE FROM invalid_resources WHERE world = ? AND x = ? AND y = ? AND z = ?")
                        .withParameter(worldId)
                        .withParameter(x)
                        .withParameter(y)
                        .withParameter(z)
                        .executeUpdate();
            }
            catch (GeneratorCreateException ignored) {
                //Invalid generator is already inserted in the database
            }
        });

    }

    @Override
    public void saveData() {
        final Database database = plugin.getDatabase();
        if (database == null) {
            logger.severe("Could not save data because database was not found.");
            return;
        }

        database.createTableIfNotExists("resources",
                "world VARCHAR(255)",
                "x INT NOT NULL",
                "y INT NOT NULL",
                "z INT NOT NULL",
                "generator VARCHAR(255) NOT NULL",
                "rotation VARCHAR(5) NOT NULL",
                "stage INT NOT NULL",
                "PRIMARY KEY (world, x, y, z)"
        );

        database.clearTable("resources");

        final DatabaseStatement insertStatement = database.merge("resources", "world, x, y, z",
                "world", "x", "y", "z", "generator", "rotation", "stage");

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

    private void registerGenerator(String worldId, int x, int y, int z, String generatorName, String rotationData, int stage) throws GeneratorCreateException {
        final World world = Bukkit.getWorld(UUID.fromString(worldId));
        if (world == null) {
            throw new GeneratorCreateException(worldId, x, y, z, "world not found");
        }

        final String worldName = world.getName();
        final GeneratorTemplate template = plugin.getGeneratorTemplate(generatorName);
        if (template == null) {
            throw new GeneratorCreateException(worldName, x, y, z, "generator template not found");
        }

        final Location origin = new Location(world, x, y, z);
        Rotation rotation = ParseUtil.parseEnumOrNull(Rotation.class, rotationData);
        if (rotation == null) {
            rotation = Rotation.NONE;
            logger.warning(String.format("Failed to load rotation of generator at %s, %d, %d, %d. Default rotation (NONE) has been applied.",
                    worldName, x, y, z));
        }

        final int maxStage = template.getMaxStage();
        if (stage > maxStage) {
            logger.warning(String.format("Failed to load stage of generator at %s, %d, %d, %d. Maximum stage (" + maxStage + ") has been applied.",
                    worldName, x, y, z));
        }

        for (Block block : template.getAllOccupiedBlocks(origin, rotation)) {
            if (plugin.getGenerators().isGenerator(block.getLocation())) {
                throw new GeneratorOverlapException(world.getName(), x, y, z);
            }
        }

        final BlockStructure lastStructure = template.getLastStage().getStructure();
        if (lastStructure.getBlockChanges().size() > 1) {
            if (largeGeneratorsPlaced >= 5) {
                throw new GeneratorLimitException();
            }
        }

        final int finalStage = Math.min(stage, template.getMaxStage());
        final Rotation finalRotation = rotation;
        plugin.getScheduler().runTask(() -> {
            try {
                Generator.create(template, origin, finalRotation, finalStage);
            } catch (GeneratorOverlapException | GeneratorLimitException ignored) {
                //Block overlaps and limits are checked before scheduling
            }
        });
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

    public void registerGenerator(@NotNull Generator generator) throws GeneratorOverlapException, GeneratorLimitException {
        GeneratorTemplate template = generator.getTemplate();
        for (Block block : template.getAllOccupiedBlocks(generator.getOrigin(), generator.getRotation())) {
            if (plugin.getGenerators().isGenerator(block.getLocation())) {
                throw new GeneratorOverlapException();
            }
        }

        final BlockStructure lastStructure = template.getLastStage().getStructure();
        if (lastStructure.getBlockChanges().size() > 1) {
            if (largeGeneratorsPlaced >= 5) {
                throw new GeneratorLimitException();
            }
            largeGeneratorsPlaced++;
        }

        generators.add(generator);
        for (Block block : generator.getAllOccupiedBlocks()) {
            generatorBlocks.put(block.getLocation(), generator);
        }
    }

    public void unregisterGenerator(@NotNull Generator generator) {
        final BlockStructure lastStructure = generator.getTemplate().getLastStage().getStructure();
        if (lastStructure.getBlockChanges().size() > 1) {
            largeGeneratorsPlaced--;
        }

        generators.remove(generator);

        for (Block block : generator.getAllOccupiedBlocks()) {
            generatorBlocks.remove(block.getLocation());
        }

        BlockStructure structure = generator.getCurrentStage().getStructure();
        structure.remove(generator.getOrigin(), generator.getRotation());

        HologramDisplay hologram = generator.getCurrentHologram();
        if (hologram != null && hologram.exists()) {
            hologram.destroy();
        }

        if (plugin.getSettings().isKeepBlocksOnRemove()) {
            generator.getTemplate().getLastStage().getStructure().place(generator.getOrigin(), generator.getRotation());
        }
    }

}
