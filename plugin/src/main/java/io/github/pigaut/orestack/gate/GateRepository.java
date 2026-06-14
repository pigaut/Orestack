package io.github.pigaut.orestack.gate;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.gate.exception.*;
import io.github.pigaut.sql.*;
import io.github.pigaut.voxel.*;
import org.bukkit.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class GateRepository {

    private static final OrestackPlugin plugin = OrestackPlugin.getInstance();

    private static void createGatesTable(@NotNull Database database) {
        database.createTableIfNotExists("gates",
                "world VARCHAR(255)",
                "x INT NOT NULL",
                "y INT NOT NULL",
                "z INT NOT NULL",
                "gate VARCHAR(255) NOT NULL",
                "rotation VARCHAR(5) NOT NULL",
                "stage INT NOT NULL",
                "transition VARCHAR(7) NOT NULL",
                "PRIMARY KEY (world, x, y, z)"
        );
    }

    private static void createInvalidGatesTable(@NotNull Database database) {
        database.createTableIfNotExists("invalid_gates",
                "world VARCHAR(255)",
                "x INT NOT NULL",
                "y INT NOT NULL",
                "z INT NOT NULL",
                "gate VARCHAR(255) NOT NULL",
                "rotation VARCHAR(5) NOT NULL",
                "stage INT NOT NULL",
                "transition VARCHAR(7) NOT NULL",
                "PRIMARY KEY (world, x, y, z)"
        );
    }

    public static void loadGates() {
        Database database = plugin.getDatabase();
        if (database == null) {
            plugin.getColoredLogger().severe("Could not load gates because database was not found.");
            return;
        }

        createGatesTable(database);
        createInvalidGatesTable(database);

        database.selectAll("gates").fetchAllRows(rowQuery -> {
            String worldId = rowQuery.getString(1);
            int x = rowQuery.getInt(2);
            int y = rowQuery.getInt(3);
            int z = rowQuery.getInt(4);
            String gateName = rowQuery.getString(5);
            String rotationData = rowQuery.getString(6);
            int phase = rowQuery.getInt(7);
            String transitionData = rowQuery.getString(8);

            try {
                plugin.getGates().registerGate(worldId, x, y, z, gateName, rotationData, phase, transitionData);
            }
            catch (GateCreateException e) {
                plugin.getColoredLogger().warning(e.getMessage());
                database.merge("invalid_gates", "world, x, y, z",
                                "world", "x", "y", "z", "gate", "rotation", "stage", "transition")
                        .withParameter(worldId)
                        .withParameter(x)
                        .withParameter(y)
                        .withParameter(z)
                        .withParameter(gateName)
                        .withParameter(rotationData)
                        .withParameter(phase)
                        .withParameter(transitionData)
                        .executeUpdate();
            }
        });

        database.selectAll("invalid_gates").fetchAllRows(rowQuery -> {
            String worldId = rowQuery.getString(1);
            int x = rowQuery.getInt(2);
            int y = rowQuery.getInt(3);
            int z = rowQuery.getInt(4);
            String gateName = rowQuery.getString(5);
            String rotationData = rowQuery.getString(6);
            int phase = rowQuery.getInt(7);
            String transitionData = rowQuery.getString(8);

            try {
                plugin.getGates().registerGate(worldId, x, y, z, gateName, rotationData, phase, transitionData);
                plugin.getColoredLogger().info(String.format("Restored gate at %s, %d, %d, %d. Reason: gate is no longer invalid.",
                        SpigotLibs.getWorldName(UUID.fromString(worldId)), x, y, z));
                database.createStatement("DELETE FROM invalid_gates WHERE world = ? AND x = ? AND y = ? AND z = ?")
                        .withParameter(worldId)
                        .withParameter(x)
                        .withParameter(y)
                        .withParameter(z)
                        .executeUpdate();
            }
            catch (GateCreateException ignored) {
                //Invalid gate is already inserted in the database
            }
        });

    }

    public static void saveGates() {
        Database database = plugin.getDatabase();
        if (database == null) {
            plugin.getColoredLogger().severe("Could not save gates because database was not found.");
            return;
        }

        createGatesTable(database);
        database.clearTable("gates");

        DatabaseStatement insertStatement = database.merge("gates", "world, x, y, z",
                "world", "x", "y", "z", "gate", "rotation", "stage", "transition");

        for (Gate gate : plugin.getGates().getAll()) {
            Location location = gate.getOrigin();
            insertStatement.withParameter(location.getWorld().getUID().toString());
            insertStatement.withParameter(location.getBlockX());
            insertStatement.withParameter(location.getBlockY());
            insertStatement.withParameter(location.getBlockZ());
            insertStatement.withParameter(gate.getTemplate().getName());
            insertStatement.withParameter(gate.getRotation().toString());
            insertStatement.withParameter(gate.getState().getCurrentPhase());
            insertStatement.withParameter(gate.getState().getTransition().toString());
            insertStatement.addBatch();
        }

        insertStatement.executeBatch();
    }

}
