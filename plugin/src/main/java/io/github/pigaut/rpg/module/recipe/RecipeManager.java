package io.github.pigaut.rpg.module.recipe;

import io.github.pigaut.rpg.player.data.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.manager.config.*;
import io.github.pigaut.rpg.player.data.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.manager.config.*;
import io.github.pigaut.rpg.plugin.manager.module.Module;
import io.github.pigaut.rpg.server.Server;
import io.github.pigaut.rpg.server.version.*;
import io.github.pigaut.rpg.server.version.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class RecipeManager extends ConfigBackedManager<RecipeTemplate> {

    private final List<RecipeTemplate> registeredRecipes = new ArrayList<>();

    private final boolean smithingRecipesSupported = Server.getVersion() >= Version.V1_20;

    public RecipeManager(@NotNull EnhancedJavaPlugin plugin) {
        super(plugin, Module.RECIPES, RecipeTemplate.class);
        extractor((section, key) -> section.getRequired(key, MultiRecipe.class).recipes());
    }

    public boolean isSmithingRecipesSupported() {
        return smithingRecipesSupported;
    }

    public @NotNull List<RecipeTemplate> getAllRegistered() {
        return new ArrayList<>(registeredRecipes);
    }

    @Override
    public void enable() {
        for (RecipeTemplate recipeTemplate : plugin.getRecipes().getAll()) {
            recipeTemplate.register();
            if (!recipeTemplate.isRegistered()) {
                continue;
            }

            registeredRecipes.add(recipeTemplate);
            NamespacedKey recipe = recipeTemplate.getKey();

            if (recipeTemplate.isGlobal() && recipeTemplate.isDiscoverAutomatically()) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.discoverRecipe(recipe);
                }
                continue;
            }

            for (Player player : Bukkit.getOnlinePlayers()) {
                PlayerData playerData = plugin.getPlayerData(player);
                playerData.runWhenLoaded(() -> {
                    if (playerData.hasUnlockedRecipe(recipe)) {
                        player.discoverRecipe(recipe);
                    }
                });
            }
        }
    }

    @Override
    public void disable() {
        for (RecipeTemplate recipeTemplate : registeredRecipes) {
            recipeTemplate.unregister();
        }
    }

    @Override
    public void clear() {
        super.clear();
        registeredRecipes.clear();
    }

}
