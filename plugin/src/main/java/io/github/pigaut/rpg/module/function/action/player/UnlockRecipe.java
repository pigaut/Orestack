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

public class UnlockRecipe implements PlayerStateAction {

    private final RecipeTemplate recipe;

    public UnlockRecipe(@NotNull RecipeTemplate recipe) {
        this.recipe = recipe;
    }

    @Override
    public void execute(@NotNull Player player, @NotNull PlayerState playerState) {
        if (!recipe.isRegistered()) {
            return;
        }
        NamespacedKey recipeKey = recipe.getKey();
        PlayerData playerData = playerState.getPlayerData();
        if (!playerData.hasUnlockedRecipe(recipeKey)) {
            playerData.addUnlockedRecipe(recipeKey);
            if (recipe.isDiscoverAutomatically()) {
                player.discoverRecipe(recipeKey);
            }
        }
    }

}
