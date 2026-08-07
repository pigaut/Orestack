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

public class LockRecipe implements PlayerStateAction {

    private final RecipeTemplate recipeTemplate;

    public LockRecipe(RecipeTemplate recipeTemplate) {
        this.recipeTemplate = recipeTemplate;
    }

    @Override
    public void execute(@NotNull Player player, @NotNull PlayerState playerState) {
        PlayerData playerData = playerState.getPlayerData();
        playerData.runWhenLoaded(() -> {
            NamespacedKey key = recipeTemplate.getKey();
            playerData.removeUnlockedRecipe(key);
            player.undiscoverRecipe(key);
        });
    }

}
