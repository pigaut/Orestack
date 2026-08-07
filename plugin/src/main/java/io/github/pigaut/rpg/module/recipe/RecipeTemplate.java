package io.github.pigaut.rpg.module.recipe;

import io.github.pigaut.rpg.plugin.manager.*;
import io.github.pigaut.rpg.plugin.manager.*;
import org.bukkit.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

public class RecipeTemplate implements Identifiable {

    private final NamespacedKey key;
    private final String name;
    private final String group;
    private final RecipeType type;
    private final boolean global;
    private final boolean discoverAutomatically;
    private final Recipe recipe;
    private boolean registered = false;

    public RecipeTemplate(NamespacedKey key, String name, String group, RecipeType type, boolean global, boolean discoverAutomatically, Recipe recipe) {
        this.key = key;
        this.name = name;
        this.group = group;
        this.type = type;
        this.global = global;
        this.discoverAutomatically = discoverAutomatically;
        this.recipe = recipe;
    }

    public @NotNull NamespacedKey getKey() {
        return key;
    }

    @Override
    public @NotNull String getName() {
        return name;
    }

    @Override
    public @Nullable String getGroup() {
        return group;
    }

    public @NotNull RecipeType getType() {
        return type;
    }

    public boolean isDiscoverAutomatically() {
        return discoverAutomatically;
    }

    public boolean isGlobal() {
        return global;
    }

    public boolean isUnlockable() {
        return !global;
    }

    public @NotNull Recipe getRecipe() {
        return recipe;
    }

    public boolean isRegistered() {
        return registered;
    }

    public void register() {
        registered = Bukkit.getRecipe(key) != null || Bukkit.addRecipe(recipe);
    }

    public void unregister() {
        Bukkit.removeRecipe(key);
        registered = false;
    }

}
