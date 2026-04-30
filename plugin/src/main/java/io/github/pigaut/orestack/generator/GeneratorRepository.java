package io.github.pigaut.orestack.generator;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.core.*;
import io.github.pigaut.orestack.generator.global.*;
import io.github.pigaut.orestack.generator.instanced.*;
import io.github.pigaut.sql.*;
import io.github.pigaut.voxel.*;
import org.bukkit.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class GeneratorRepository {

    private static final OrestackPlugin plugin = OrestackPlugin.getInstance();

    public static void loadGenerators() {
        Database database = plugin.getDatabase();
        if (database == null) {
            plugin.getColoredLogger().severe("Could not load data because database was not found.");
            return;
        }

        createResourcesTable(database);
        createInvalidResourcesTable(database);

        database.selectAll("resources")
                .fetchAllRows(rowQuery -> {
                    String worldId = rowQuery.getString(1);
                    int x = rowQuery.getInt(2);
                    int y = rowQuery.getInt(3);
                    int z = rowQuery.getInt(4);
                    String generatorName = rowQuery.getString(5);
                    String rotationData = rowQuery.getString(6);
                    int phase = rowQuery.getInt(7);
                    boolean global = rowQuery.getBoolean(8);

                    try {
                        plugin.getGenerators().registerGenerator(worldId, x, y, z, generatorName, rotationData, phase, global);
                    } catch (GeneratorCreateException e) {
                        plugin.getColoredLogger().warning(e.getMessage());
                        database.merge("invalid_resources", "world, x, y, z",
                                        "world", "x", "y", "z", "generator", "rotation", "stage", "global")
                                .withParameter(worldId)
                                .withParameter(x)
                                .withParameter(y)
                                .withParameter(z)
                                .withParameter(generatorName)
                                .withParameter(rotationData)
                                .withParameter(phase)
                                .withParameter(global)
                                .executeUpdate();
                    }
                });

        database.selectAll("invalid_resources")
                .fetchAllRows(rowQuery -> {
                    String worldId = rowQuery.getString(1);
                    int x = rowQuery.getInt(2);
                    int y = rowQuery.getInt(3);
                    int z = rowQuery.getInt(4);
                    String generatorName = rowQuery.getString(5);
                    String rotationData = rowQuery.getString(6);
                    int phase = rowQuery.getInt(7);
                    boolean global = rowQuery.getBoolean(8);

                    try {
                        plugin.getGenerators().registerGenerator(worldId, x, y, z, generatorName, rotationData, phase, global);
                        plugin.getColoredLogger().info(String.format("Restored generator at %s, %d, %d, %d. Reason: generator is no longer invalid.",
                                SpigotLibs.getWorldName(UUID.fromString(worldId)), x, y, z));
                        database.createStatement("DELETE FROM invalid_resources WHERE world = ? AND x = ? AND y = ? AND z = ?")
                                .withParameter(worldId)
                                .withParameter(x)
                                .withParameter(y)
                                .withParameter(z)
                                .executeUpdate();
                    } catch (GeneratorCreateException ignored) {
                        //Invalid generator is already inserted in the database
                    }
                });
    }

    public static void saveGenerators() {
        Database database = plugin.getDatabase();
        if (database == null) {
            plugin.getColoredLogger().severe("Could not load data because database was not found.");
            return;
        }

        createResourcesTable(database);
        database.clearTable("resources");

        DatabaseStatement insertStatement = database.merge("resources", "world, x, y, z",
                "world", "x", "y", "z", "generator", "rotation", "stage", "global");

        for (GlobalGenerator generator : plugin.getGenerators().getAllGlobal()) {
            Location location = generator.getOrigin();
            insertStatement.withParameter(location.getWorld().getUID().toString());
            insertStatement.withParameter(location.getBlockX());
            insertStatement.withParameter(location.getBlockY());
            insertStatement.withParameter(location.getBlockZ());
            insertStatement.withParameter(generator.getTemplate().getName());
            insertStatement.withParameter(generator.getRotation().toString());
            insertStatement.withParameter(generator.getState().getCurrentPhase());
            insertStatement.withParameter(true);
            insertStatement.addBatch();
        }

        for (VirtualGenerator virtualGenerator : plugin.getGenerators().getAllVirtual()) {
            Location location = virtualGenerator.getOrigin();
            insertStatement.withParameter(location.getWorld().getUID().toString());
            insertStatement.withParameter(location.getBlockX());
            insertStatement.withParameter(location.getBlockY());
            insertStatement.withParameter(location.getBlockZ());
            insertStatement.withParameter(virtualGenerator.getTemplate().getName());
            insertStatement.withParameter(virtualGenerator.getRotation().toString());
            insertStatement.withParameter(virtualGenerator.getMaxPhase());
            insertStatement.withParameter(false);
            insertStatement.addBatch();
        }

        insertStatement.executeBatch();
    }

    private static void createResourcesTable(@NotNull Database database) {
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

        database.addColumnIfNotExists("resources", "global BOOLEAN NOT NULL DEFAULT TRUE");
    }

    private static void createInvalidResourcesTable(@NotNull Database database) {
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

        database.addColumnIfNotExists("invalid_resources", "global BOOLEAN NOT NULL DEFAULT TRUE");
    }

}
