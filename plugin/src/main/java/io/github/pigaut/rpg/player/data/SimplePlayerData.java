package io.github.pigaut.rpg.player.data;

import org.bukkit.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class SimplePlayerData implements PlayerData {

    private final UUID playerId;
    private Set<NamespacedKey> unlockedRecipes = new HashSet<>();

    private boolean loaded = false;
    private final List<Runnable> pendingTasks = new ArrayList<>();

    public SimplePlayerData(@NotNull UUID playerId) {
        this.playerId = playerId;
    }

    @Override
    public @NotNull UUID getUniqueId() {
        return playerId;
    }

    @Override
    public boolean isLoaded() {
        return loaded;
    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
        if (loaded) {
            for (Runnable pendingTask : pendingTasks) {
                pendingTask.run();
            }
            pendingTasks.clear();
        }
    }

    @Override
    public void runWhenLoaded(@NotNull Runnable task) {
        if (loaded) {
            task.run();
        } else {
            pendingTasks.add(task);
        }
    }

    @Override
    public @NotNull Set<NamespacedKey> getUnlockedRecipes() {
        return new HashSet<>(unlockedRecipes);
    }

    @Override
    public boolean hasUnlockedRecipe(@NotNull NamespacedKey recipe) {
        return unlockedRecipes.contains(recipe);
    }

    @Override
    public void addUnlockedRecipe(@NotNull NamespacedKey recipe) {
        unlockedRecipes.add(recipe);
    }

    @Override
    public void removeUnlockedRecipe(@NotNull NamespacedKey recipe) {
        unlockedRecipes.remove(recipe);
    }

    public void setUnlockedRecipes(@NotNull Set<NamespacedKey> unlockedRecipes) {
        this.unlockedRecipes = new HashSet<>(unlockedRecipes);
    }


}
