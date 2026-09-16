package io.github.pigaut.rpg.module.recipe.detail;

import org.bukkit.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.*;

import java.util.*;

public interface Ingredient {

    @NotNull Ingredient EMPTY = new Ingredient() {
        @Override
        public int match(@NotNull ItemStack item) {
            return 1;
        }

        @Override
        public List<Material> getMaterialChoices() {
            return List.of();
        }
    };

    /**
     * Checks whether the given item satisfies this ingredient, and if so,
     * how many units of it are required.
     *
     * @param item the item to check; never {@code null}
     * @return the amount of {@code item} required to satisfy this ingredient,
     *         or {@code -1} if the item does not match this ingredient at all
     */
    int match(@NotNull ItemStack item);

    /**
     * Returns the materials this ingredient can accept, for use in vanilla
     * recipe registration (e.g. {@link org.bukkit.inventory.RecipeChoice.MaterialChoice}).
     *
     * @return the list of acceptable materials for this ingredient
     */
    List<Material> getMaterialChoices();

}
