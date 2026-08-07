package io.github.pigaut.rpg.module.mob.spawnpad;

import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.bukkit.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.sql.*;
import io.github.pigaut.rpg.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.yaml.delay.*;
import org.bukkit.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class MobSpawnPadRepository {

    public static void loadMobSpawnPads(@NotNull EnhancedPlugin plugin) {
        Database database = plugin.getDatabase();
        if (database == null) {
            plugin.getColoredLogger().severe("Could not load data because database was not found.");
            return;
        }

        createSpawnPadsTable(database);
        createInvalidSpawnPadsTable(database);

        database.selectAll("mob_spawn_pads")
                .fetchAllRows(rowQuery -> {
                    String worldId = rowQuery.getString(1);
                    int x = rowQuery.getInt(2);
                    int y = rowQuery.getInt(3);
                    int z = rowQuery.getInt(4);
                    String mobName = rowQuery.getString(5);
                    String spawnDelayData = rowQuery.getString(6);
                    double spawnRange = rowQuery.getDouble(7);
                    double activationRange = rowQuery.getDouble(8);

                    try {
                        plugin.getMobSpawnPads().register(worldId, x, y, z, mobName, spawnDelayData, spawnRange, activationRange);
                    } catch (MobSpawnPadCreateException e) {
                        plugin.getColoredLogger().warning(e.getMessage());
                        database.merge("invalid_mob_spawn_pads", "world, x, y, z",
                                        "world", "x", "y", "z", "mob_template", "spawn_delay", "spawn_range", "activation_range")
                                .withParameter(worldId)
                                .withParameter(x)
                                .withParameter(y)
                                .withParameter(z)
                                .withParameter(mobName)
                                .withParameter(spawnDelayData)
                                .withParameter(spawnRange)
                                .withParameter(activationRange)
                                .executeUpdate();
                    }
                });

        database.selectAll("invalid_mob_spawn_pads")
                .fetchAllRows(rowQuery -> {
                    String worldId = rowQuery.getString(1);
                    int x = rowQuery.getInt(2);
                    int y = rowQuery.getInt(3);
                    int z = rowQuery.getInt(4);
                    String mobName = rowQuery.getString(5);
                    String spawnDelayData = rowQuery.getString(6);
                    double spawnRange = rowQuery.getDouble(7);
                    double activationRange = rowQuery.getDouble(8);

                    try {
                        plugin.getMobSpawnPads().register(worldId, x, y, z, mobName, spawnDelayData, spawnRange, activationRange);
                        plugin.getColoredLogger().info(String.format("Restored mob spawn pad at %s, %d, %d, %d. Reason: mob spawn pad is no longer invalid.",
                                LocationUtil.getWorldName(UUID.fromString(worldId)), x, y, z));

                        database.createStatement("DELETE FROM invalid_mob_spawn_pads WHERE world = ? AND x = ? AND y = ? AND z = ?")
                                .withParameter(worldId)
                                .withParameter(x)
                                .withParameter(y)
                                .withParameter(z)
                                .executeUpdate();
                    } catch (MobSpawnPadCreateException ignored) {
                        // Spawn pad is still invalid
                    }
                });
    }

    public static void saveMobSpawnPads(@NotNull EnhancedPlugin plugin) {
        Database database = plugin.getDatabase();
        if (database == null) {
            plugin.getColoredLogger().severe("Could not save data because database was not found.");
            return;
        }

        createSpawnPadsTable(database);
        database.clearTable("mob_spawn_pads");

        DatabaseStatement insertStatement = database.merge("mob_spawn_pads", "world, x, y, z",
                "world", "x", "y", "z", "mob_template", "spawn_delay", "spawn_range", "activation_range");

        for (MobSpawnPad spawnPad : plugin.getMobSpawnPads().getAll()) {
            Location location = spawnPad.getLocation();
            insertStatement.withParameter(location.getWorld().getUID().toString());
            insertStatement.withParameter(location.getBlockX());
            insertStatement.withParameter(location.getBlockY());
            insertStatement.withParameter(location.getBlockZ());
            insertStatement.withParameter(spawnPad.getMobTemplate().getName());

            Delay spawnDelay = spawnPad.getSpawnDelay();
            insertStatement.withParameter(spawnDelay != null ? spawnDelay.toString() : null);

            insertStatement.withParameter(spawnPad.getSpawnRange());
            insertStatement.withParameter(spawnPad.getActivationRange());
            insertStatement.addBatch();
        }

        insertStatement.executeBatch();
    }

    private static void createSpawnPadsTable(@NotNull Database database) {
        database.createTableIfNotExists("mob_spawn_pads",
                "world VARCHAR(255)",
                "x INT NOT NULL",
                "y INT NOT NULL",
                "z INT NOT NULL",
                "mob_template VARCHAR(255) NOT NULL",
                "spawn_delay VARCHAR(255) DEFAULT NULL",
                "spawn_range DOUBLE NOT NULL",
                "activation_range DOUBLE NOT NULL",
                "PRIMARY KEY (world, x, y, z)"
        );
    }

    private static void createInvalidSpawnPadsTable(@NotNull Database database) {
        database.createTableIfNotExists("invalid_mob_spawn_pads",
                "world VARCHAR(255)",
                "x INT NOT NULL",
                "y INT NOT NULL",
                "z INT NOT NULL",
                "mob_template VARCHAR(255) NOT NULL",
                "spawn_delay VARCHAR(255) DEFAULT NULL",
                "spawn_range DOUBLE NOT NULL",
                "activation_range DOUBLE NOT NULL",
                "PRIMARY KEY (world, x, y, z)"
        );
    }
}
