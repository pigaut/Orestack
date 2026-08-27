package io.github.pigaut.rpg.player.data.repository;

import io.github.pigaut.rpg.player.data.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.sql.*;
import org.bukkit.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class UnlockedRecipesRepository<T extends SimplePlayerData> implements PlayerDataRepository<T> {

    private final EnhancedJavaPlugin plugin;

    public UnlockedRecipesRepository(EnhancedJavaPlugin plugin) {
        this.plugin = plugin;
    }

    private void createRecipesTable() {
        Database database = plugin.getDatabase();
        if (database == null) {
            return;
        }

        database.createTableIfNotExists("player_recipes",
                "uuid VARCHAR(36) NOT NULL",
                "recipe_key VARCHAR(255) NOT NULL",
                "PRIMARY KEY (uuid, recipe_key)"
        );
    }

    @Override
    public void loadData(@NotNull SimplePlayerData playerData) {
        Database database = plugin.getDatabase();
        if (database == null) {
            return;
        }

        createRecipesTable();

        String uuidString = playerData.getUniqueId().toString();
        Set<NamespacedKey> recipes = new HashSet<>();

        database.createStatement("SELECT recipe_key FROM player_recipes WHERE uuid = ?")
                .withParameter(uuidString)
                .executeQuery(rowQuery -> {
                    while (rowQuery.next()) {
                        String keyStr = rowQuery.getString(1);
                        NamespacedKey key = NamespacedKey.fromString(keyStr);
                        if (key != null) {
                            recipes.add(key);
                        }
                    }
                });

        playerData.setUnlockedRecipes(recipes);
    }

    @Override
    public void saveData(@NotNull SimplePlayerData playerData) {
        Database database = plugin.getDatabase();
        if (database == null) {
            return;
        }

        createRecipesTable();

        String uuidString = playerData.getUniqueId().toString();

        database.createStatement("DELETE FROM player_recipes WHERE uuid = ?")
                .withParameter(uuidString)
                .executeUpdate();

        DatabaseStatement insertStatement = database.createStatement(
                "INSERT INTO player_recipes (uuid, recipe_key) VALUES (?, ?)"
        );

        for (NamespacedKey key : playerData.getUnlockedRecipes()) {
            insertStatement.withParameter(uuidString);
            insertStatement.withParameter(key.toString());
            insertStatement.addBatch();
        }

        insertStatement.executeBatch();
    }

    @Override
    public void clearData(@NotNull T playerData) {
        playerData.setUnlockedRecipes(Set.of());
    }

}
