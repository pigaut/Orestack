package io.github.pigaut.rpg.player.data;

import org.bukkit.*;
import org.jetbrains.annotations.*;

import java.util.*;

public interface PlayerData {

    @NotNull
    UUID getUniqueId();

    boolean isLoaded();

    void setLoaded(boolean loaded);

    void runWhenLoaded(@NotNull Runnable task);

    @NotNull
    Set<NamespacedKey> getUnlockedRecipes();

    boolean hasUnlockedRecipe(@NotNull NamespacedKey recipe);

    void addUnlockedRecipe(@NotNull NamespacedKey recipe);

    void removeUnlockedRecipe(@NotNull NamespacedKey recipe);

}
