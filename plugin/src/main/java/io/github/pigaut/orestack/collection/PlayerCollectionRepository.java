package io.github.pigaut.orestack.collection;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.collection.template.*;
import io.github.pigaut.orestack.player.data.*;
import io.github.pigaut.sql.*;
import io.github.pigaut.voxel.player.data.*;
import io.github.pigaut.voxel.plugin.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class PlayerCollectionRepository implements PlayerDataRepository<SimpleRpgPlayerData> {

    private final OrestackPlugin plugin;

    public PlayerCollectionRepository(OrestackPlugin plugin) {
        this.plugin = plugin;
    }

    private void createCollectionTable(Database database) {
        database.createTableIfNotExists("player_collections",
                "uuid VARCHAR(36) NOT NULL",
                "collection VARCHAR(255) NOT NULL",
                "amount INT NOT NULL",
                "PRIMARY KEY (uuid, collection)"
        );
    }

    @Override
    public void loadData(@NotNull SimpleRpgPlayerData playerData) {
        Database database = plugin.getDatabase();
        if (database == null) {
            return;
        }

        createCollectionTable(database);

        UUID playerId = playerData.getUniqueId();
        String uuidString = playerId.toString();

        Set<Collection> itemCollections = new HashSet<>();
        database.createStatement("SELECT collection, amount FROM player_collections WHERE uuid = ?")
                .withParameter(uuidString)
                .fetchAllRows(row -> {
                    String collectionName = row.getString("collection");

                    CollectionTemplate collectionTemplate = plugin.getCollectionTemplate(collectionName);
                    if (collectionTemplate == null) {
                        plugin.getColoredLogger().warning("Could not load player item collection. " +
                                "Reason: could not find collection with name: " + collectionName);
                        return;
                    }

                    int collectedAmount = row.getInt("amount");
                    if (collectedAmount < 0) {
                        plugin.getColoredLogger().warning("Could not load player collection amount for: " +
                                collectionName + ". Applying default amount of 0.");
                        collectedAmount = 0;
                    }

                    itemCollections.add(new Collection(collectionTemplate, collectedAmount));
                });

        // Add missing collections
        for (CollectionTemplate collectionTemplate : plugin.getCollectionTemplates().getAll()) {
            boolean foundCollection = false;
            for (Collection collection : itemCollections) {
                if (collection.getName().equals(collectionTemplate.getName())) {
                    foundCollection = true;
                    break;
                }
            }
            if (!foundCollection) {
                itemCollections.add(new Collection(collectionTemplate, 0));
            }
        }

        playerData.setItemCollections(itemCollections);
    }

    @Override
    public void saveData(@NotNull SimpleRpgPlayerData playerData) {
        Database database = plugin.getDatabase();
        if (database == null) {
            return;
        }

        createCollectionTable(database);

        String uuidString = playerData.getUniqueId().toString();

        database.createStatement("DELETE FROM player_collections WHERE uuid = ?")
                .withParameter(uuidString)
                .executeUpdate();

        DatabaseStatement insertStatement = database.insert("player_collections", "uuid", "collection", "amount");
        for (Collection collection : playerData.getItemCollections()) {
            insertStatement.withParameter(uuidString);
            insertStatement.withParameter(collection.getName());
            insertStatement.withParameter(collection.getCollectedAmount());
            insertStatement.addBatch();
        }
        insertStatement.executeBatch();
    }
}
