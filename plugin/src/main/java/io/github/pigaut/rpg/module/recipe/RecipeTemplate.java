package io.github.pigaut.rpg.module.recipe;

import io.github.pigaut.rpg.module.recipe.detail.*;
import io.github.pigaut.rpg.plugin.manager.*;
import org.bukkit.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

public class RecipeTemplate implements Identifiable {

    private final NamespacedKey key;
    private final String name;
    private final String group;
    private final RecipeType type;
    private final IngredientMatcher ingredientMatcher;
    private final boolean locked;
    private final boolean discoverAutomatically;
    private final Recipe recipe;
    private boolean registered = false;

    public RecipeTemplate(@NotNull NamespacedKey key, @NotNull String name, @Nullable String group,
                          @NotNull RecipeType type, @Nullable IngredientMatcher ingredientMatcher,
                          boolean locked, boolean discoverAutomatically,
                          @NotNull Recipe recipe) {
        this.key = key;
        this.name = name;
        this.group = group;
        this.type = type;
        this.ingredientMatcher = ingredientMatcher;
        this.locked = locked;
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

    public @Nullable IngredientMatcher getIngredientMatcher() {
        return ingredientMatcher;
    }

    public boolean isDiscoverAutomatically() {
        return discoverAutomatically;
    }

    public boolean isLocked() {
        return locked;
    }

    public boolean isUnlockable() {
        return locked;
    }

    public @NotNull Recipe getRecipe() {
        return recipe;
    }

    public boolean isRegistered() {
        return registered;
    }

    public void register() {
        Bukkit.removeRecipe(key);
        registered = Bukkit.addRecipe(recipe);
    }

    public void unregister() {
        Bukkit.removeRecipe(key);
        registered = false;
    }

}
