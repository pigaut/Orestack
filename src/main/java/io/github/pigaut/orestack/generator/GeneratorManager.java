package io.github.pigaut.orestack.generator;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.generator.template.*;
import io.github.pigaut.orestack.util.*;
import io.github.pigaut.sql.*;
import io.github.pigaut.voxel.core.structure.*;
import io.github.pigaut.voxel.hologram.*;
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
    private final Set<Generator> generators = new HashSet<>();
    private final Map<Location, Generator> generatorBlocks = new ConcurrentHashMap<>();
    private int largeGeneratorsPlaced = 0;

    public GeneratorManager(OrestackPlugin plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    public int getLargeGeneratorsPlaced() {
        return largeGeneratorsPlaced;
    }

    @Override
    public void disable() {
        for (Generator blockGenerator : generators) {
            blockGenerator.cancelGrowth();
            for (Block block : blockGenerator.getAllOccupiedBlocks()) {
                block.setType(Material.AIR, false);
            }
            final HologramDisplay hologramDisplay = blockGenerator.getCurrentHologram();
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

        final Database database = plugin.getDatabase();
        if (database == null) {
            plugin.getLogger().severe("Could not load data because database was not found.");
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

        database.selectAll("resources").fetchAllRows(rowQuery -> {
            final String worldId = rowQuery.getString(1);
            final int x = rowQuery.getInt(2);
            final int y = rowQuery.getInt(3);
            final int z = rowQuery.getInt(4);
            final String generatorName = rowQuery.getString(5);
            final String rotationData = rowQuery.getString(6);
            final int stage = rowQuery.getInt(7);

            final World world = Bukkit.getWorld(UUID.fromString(worldId));
            if (world == null) {
                logger.warning("Ignored generator at " + world.getName() + ", " + x + ", " + y + ", " + z + ". " +
                        "Reason: world does not exist.");
                return;
            }

            final GeneratorTemplate template = plugin.getGeneratorTemplate(generatorName);
            if (template == null) {
                logger.warning("Ignored generator at " + world.getName() + ", " + x + ", " + y + ", " + z + ". " +
                        "Reason: generator template does not exist.");
                return;
            }

            final Location origin = new Location(world, x, y, z);
            Rotation rotation = Deserializers.getEnum(Rotation.class, rotationData);
            if (rotation == null) {
                rotation = Rotation.NONE;
                logger.warning("Reset generator rotation at " + world.getName() + ", " + x + ", " + y + ", " + z + ". " +
                        "Reason: invalid rotation data.");
            }

            final int maxStage = template.getMaxStage();
            if (stage > maxStage) {
                logger.warning("Reset generator stage at " + world.getName() + ", " + x + ", " + y + ", " + z + ". " +
                        "Reason: stage data surpasses the current max stage.");
            }

            final Rotation finalRotation = rotation;
            plugin.getScheduler().runTask(() -> {
                try {
                    Generator.create(template, origin, finalRotation, Math.min(stage, maxStage));
                }
                catch (GeneratorOverlapException e) {
                    logger.warning("Removed generator at " + world.getName() + ", " + x + ", " + y + ", " + z + ". " +
                            "Reason: generators overlapped.");
                }
                catch (GeneratorLimitException e) {
                    logger.warning("Removed generator at " + world.getName() + ", " + x + ", " + y + ", " + z + ". " +
                            "Reason: exceeded multi-block generator limit.");
                }
            });
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
        final GeneratorTemplate template = generator.getTemplate();
        for (Block block : template.getAllOccupiedBlocks(generator.getOrigin(), generator.getRotation())) {
            if (plugin.getGenerators().isGenerator(block.getLocation())) {
                throw new GeneratorOverlapException();
            }
        }

        final BlockStructure lastStructure = template.getLastStage().getStructure();
        if (lastStructure.getBlockChanges().size() > 1) {
            if (largeGeneratorsPlaced >= 25) {
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

        final BlockStructure structure = generator.getCurrentStage().getStructure();
        structure.removeBlocks(generator.getOrigin(), generator.getRotation());

        final HologramDisplay hologram = generator.getCurrentHologram();
        if (hologram != null && hologram.exists()) {
            hologram.destroy();
        }
    }

}
