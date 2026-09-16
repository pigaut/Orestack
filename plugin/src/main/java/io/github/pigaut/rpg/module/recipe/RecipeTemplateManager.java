package io.github.pigaut.rpg.module.recipe;

import io.github.pigaut.rpg.player.data.*;
import io.github.pigaut.rpg.plugin.*;
import io.github.pigaut.rpg.plugin.manager.config.*;
import io.github.pigaut.rpg.plugin.manager.module.Module;
import io.github.pigaut.rpg.server.Server;
import io.github.pigaut.rpg.server.version.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class RecipeTemplateManager extends ConfigBackedManager<RecipeTemplate> {

    private final List<RecipeTemplate> registeredRecipes = new ArrayList<>();

    private final boolean smithingRecipesSupported = Server.getVersion() >= Version.V1_20;

    public RecipeTemplateManager(@NotNull EnhancedJavaPlugin plugin) {
        super(plugin, Module.RECIPES, RecipeTemplate.class);
        extractor((section, key) -> section.getRequired(key, MultiRecipe.class).recipes());
    }

    public boolean isSmithingRecipesSupported() {
        return smithingRecipesSupported;
    }

    public @NotNull List<RecipeTemplate> getAllRegistered() {
        return new ArrayList<>(registeredRecipes);
    }

    public @Nullable RecipeTemplate get(@NotNull Recipe recipe) {
        if (!(recipe instanceof Keyed keyedRecipe)) {
            return null;
        }

        NamespacedKey recipeKey = keyedRecipe.getKey();
        if (!recipeKey.getNamespace().equals(plugin.getNamespace())) {
            return null;
        }

        return get(recipeKey.getKey());
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

            if (!recipeTemplate.isLocked() && recipeTemplate.isDiscoverAutomatically()) {
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
        registeredRecipes.clear();
    }

}
