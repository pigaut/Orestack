package io.github.pigaut.rpg.module.function.action.recipe;

import io.github.pigaut.rpg.player.data.*;
import io.github.pigaut.rpg.player.state.*;
import io.github.pigaut.rpg.module.function.action.player.state.*;
import io.github.pigaut.rpg.module.recipe.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class LockRecipes implements PlayerStateAction {

    private final List<RecipeTemplate> recipes;

    public LockRecipes(List<RecipeTemplate> recipes) {
        this.recipes = recipes;
    }

    @Override
    public void execute(@NotNull Player player, @NotNull PlayerState playerState) {
        PlayerData playerData = playerState.getPlayerData();
        for (RecipeTemplate recipe : recipes) {
            playerData.runWhenLoaded(() -> {
                NamespacedKey recipeKey = recipe.getKey();
                playerData.removeUnlockedRecipe(recipeKey);
                player.undiscoverRecipe(recipeKey);
            });
        }
    }

}
