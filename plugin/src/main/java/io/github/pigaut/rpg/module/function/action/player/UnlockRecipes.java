package io.github.pigaut.rpg.module.function.action.player;

import io.github.pigaut.rpg.player.data.*;
import io.github.pigaut.rpg.player.state.*;
import io.github.pigaut.rpg.module.function.action.player.state.*;
import io.github.pigaut.rpg.module.recipe.*;
import io.github.pigaut.rpg.module.function.action.player.state.*;
import io.github.pigaut.rpg.module.recipe.*;
import io.github.pigaut.rpg.player.data.*;
import io.github.pigaut.rpg.player.state.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class UnlockRecipes implements PlayerStateAction {

    private final List<RecipeTemplate> recipes;

    public UnlockRecipes(@NotNull List<RecipeTemplate> recipes) {
        this.recipes = recipes;
    }

    @Override
    public void execute(@NotNull Player player, @NotNull PlayerState playerState) {
        for (RecipeTemplate recipe : recipes) {
            if (!recipe.isRegistered()) {
                return;
            }
            NamespacedKey recipeKey = recipe.getKey();
            PlayerData playerData = playerState.getPlayerData();
            if (!playerData.hasUnlockedRecipe(recipeKey)) {
                playerData.addUnlockedRecipe(recipeKey);
                player.discoverRecipe(recipeKey);
            }
        }
    }

}
