package io.github.pigaut.rpg.module.function.condition.player;

import org.bukkit.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

public class PlayerHasDiscoveredRecipe implements PlayerCondition {

    private final NamespacedKey recipe;

    public PlayerHasDiscoveredRecipe(NamespacedKey recipe) {
        this.recipe = recipe;
    }

    @Override
    public @Nullable Boolean evaluate(@NotNull Player player) {
        return player.hasDiscoveredRecipe(recipe);
    }

}
